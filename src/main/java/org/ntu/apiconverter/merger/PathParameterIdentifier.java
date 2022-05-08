package org.ntu.apiconverter.merger;

import javafx.util.Pair;
import org.ntu.apiconverter.entity.*;

import java.util.regex.Pattern;

import java.util.*;

public class PathParameterIdentifier {

    /**
     * identify path parameters and update path of api doc entries
     * @param apiDoc
     * @return
     */
    public ApiDoc update(ApiDoc apiDoc) {
        List<ApiDocEntry> apiDocEntries = apiDoc.getApiDocEntries();

        // 1. normalise
        List<UrlGroup> urlGroups = normalize(apiDocEntries);

        // 2. identify
        List<Pair<Integer, List<List<Token>>>> pathSegments = identify(urlGroups);

        // 3. reconstruct
        HashSet<List<Token>> patternWithoutParams = new HashSet<>();
        HashSet<List<Token>> patternWithParams = new HashSet<>();
        reconstruct(pathSegments, patternWithoutParams, patternWithParams);

        // 4. update api doc
        updateApiDocEntries(apiDocEntries, patternWithoutParams, patternWithParams);

        return apiDoc;
    }

    /**
     * normalise url based on decreasing order of token frequency and construct url groups based on length
     * @param entries
     * @return
     */
    private List<UrlGroup> normalize(List<ApiDocEntry> entries) {
        // 1. count token frequency
        HashMap<String, Integer> counter = new HashMap<>();
        for (ApiDocEntry entry : entries) {
            String[] segments = entry.getPath().split("/");
            for (String segment : segments)
                if (!Objects.equals(segment, "")) counter.put(segment, counter.getOrDefault(segment, 0) + 1);
        }

        // 2. construct urls with token freq
        HashMap<Integer, UrlGroup> urlGroupHashMap = new HashMap<>();
        for (ApiDocEntry entry : entries) {
            String[] segments = entry.getPath().split("/");
            ArrayList<Token> tokens = new ArrayList<>();
            for (int i = 0; i < segments.length; i++)
                if (!segments[i].equals("")) tokens.add(new Token(segments[i], i, counter.get(segments[i])));

            if (!urlGroupHashMap.containsKey(tokens.size()))
                urlGroupHashMap.put(tokens.size(), new UrlGroup(tokens.size()));
            urlGroupHashMap.get(tokens.size()).addUrl(new Url(tokens));
        }

        return new ArrayList<>(urlGroupHashMap.values());
    }

    /**
     * Return a List of Set of path segments identified as path, instead of parameters
     *
     * @param urlGroups
     * @return
     */
    private List<Pair<Integer, List<List<Token>>>> identify(List<UrlGroup> urlGroups) {
        List<Pair<Integer, List<List<Token>>>> pathSet = new ArrayList<>();
        ArrayList<RamerDouglasPeucker.Point> pointList = new ArrayList<>();
        ArrayList<RamerDouglasPeucker.Point> resPointList = new ArrayList<>();
        int thresholdIndex;

        for (UrlGroup urlGroup : urlGroups) {
            int length = urlGroup.getLength();
            HashMap<List<Token>, Integer> pathSegments = new HashMap<>();

            for (int n = length - 1; n >= 1; n--) {
                // 1. sort ngrams based on frequency & reorder
                List<Pair<List<Token>, Integer>> nGrams = new ArrayList<>();
                getNGramsList(n, urlGroup, length).forEach((k, v) -> nGrams.add(new Pair<>(k, v)));
                nGrams.sort(new Comparator<Pair<List<Token>, Integer>>() {
                    @Override
                    public int compare(Pair<List<Token>, Integer> p1, Pair<List<Token>, Integer> p2) {
                        return Integer.compare(p1.getValue(), p2.getValue());
                    }
                });

                // 2. curve simplification with RDP algorithm
                pointList.clear();
                resPointList.clear();
                for (int i = 0; i < nGrams.size(); i++)
                    pointList.add(new RamerDouglasPeucker.Point((double) i, (double) nGrams.get(i).getValue()));
                RamerDouglasPeucker.ramerDouglasPeucker(pointList, 1, resPointList);

                // 3. calculate turning point on the simplified curve
                thresholdIndex = getTurningIndex(resPointList);

                // 4. add to path segments & remove url containing tokens already marked as path
                for (int i = thresholdIndex; i < nGrams.size(); i++) {
                    pathSegments.put(nGrams.get(i).getKey(), n);
                    urlGroup.removeUrls(nGrams.get(i).getKey());
                }
            }
            pathSet.add(new Pair<>(urlGroup.getLength(), new ArrayList<>(pathSegments.keySet())));
        }

        return pathSet;
    }

    /**
     * calculate the index of ngrams where there is a sharp increase of frequency
     * @param pointList
     * @return
     */
    private int getTurningIndex(ArrayList<RamerDouglasPeucker.Point> pointList) {
        for (int i = 0; i < pointList.size() - 1; i++) {
            RamerDouglasPeucker.Point point1 = pointList.get(i);
            RamerDouglasPeucker.Point point2 = pointList.get(i + 1);

            double angle = Math.atan((point2.getValue() - point1.getValue()) / (point2.getKey() - point1.getKey()));
            if (angle > Math.PI / 3) return i + 1;
        }
        return pointList.size();
    }

    /**
     * given a url group, calculate n gram list
     * @param n
     * @param urlGroup
     * @param length
     * @return
     */
    private HashMap<List<Token>, Integer> getNGramsList(int n, UrlGroup urlGroup, int length) {
        ArrayList<Url> urlList = urlGroup.getUrlList();
        HashMap<List<Token>, Integer> res = new HashMap<>();

        for (int i = 0; i < length - n; i++) {
            for (Url url : urlList) {
                List<Token> ngram = url.getTokenList().subList(i, i + n);
                res.put(ngram, res.getOrDefault(ngram, 0) + 1);
            }
        }

        return res;
    }

    /**
     * reconstruct url by replacing certain segments with params indicator
     * @param pathSegments
     * @param patternWithoutParams
     * @param patternWithParams
     */
    private void reconstruct(List<Pair<Integer, List<List<Token>>>> pathSegments, HashSet<List<Token>> patternWithoutParams, HashSet<List<Token>> patternWithParams) {
        for (Pair<Integer, List<List<Token>>> pair : pathSegments) {
            for (List<Token> nGram : pair.getValue()) {
                List<Token> pattern = getNewEmptyPattern(pair.getKey());
                for (Token token : nGram)
                    pattern.get(token.getIndex()).setValue(token.getValue());
                if (nGram.size() == pair.getKey()) patternWithoutParams.add(pattern);
                else patternWithParams.add(pattern);
            }
        }
    }

    /**
     * create new empty pattern
     * @param length
     * @return
     */
    private ArrayList<Token> getNewEmptyPattern(int length) {
        ArrayList<Token> pattern = new ArrayList<>();
        for (int i = 0; i < length; i++) pattern.add(new Token("{param_" + i + "}"));
        return pattern;
    }

    /**
     * update path of each api doc entry if they contain parameters and
     * remove api doc entries with duplicated path from api doc
     * @param apiDocEntries
     * @param patternWithoutParams
     * @param patternWithParams
     */
    private void updateApiDocEntries(List<ApiDocEntry> apiDocEntries, HashSet<List<Token>> patternWithoutParams, HashSet<List<Token>> patternWithParams) {
        // 1. preprocess patternWithParams and patternWithoutParams
        List<String> pathWithoutParams = new ArrayList<>();
        List<String> pathWithParams = new ArrayList<>();
        ArrayList<String> path = new ArrayList<>();

        for (List<Token> pattern : patternWithoutParams) {
            path.clear();
            for (Token token : pattern) path.add(token.getValue());
            pathWithoutParams.add(String.join("/", path));
        }

        for (List<Token> pattern : patternWithParams) {
            path.clear();
            for (Token token : pattern) path.add(token.getValue());
            pathWithParams.add(String.join("/", path));
        }

        // 2. update path for apiDocEntries
        Pattern regex = Pattern.compile("[{]param_([0-9])+[}]");
        for (ApiDocEntry entry : apiDocEntries) {
            if (!pathWithoutParams.contains(entry.getPath())) {
                HashSet<String> entryPathTokens = new HashSet<>(Arrays.asList(entry.getPath().split("/")));
                for (String pathString : pathWithParams) {
                    HashSet<String> patternPathTokens = new HashSet<>(Arrays.asList(pathString.split("/")));
                    boolean flag = true;
                    patternPathTokens.removeAll(entryPathTokens);
                    for (String remainder : patternPathTokens)
                        if (!regex.matcher(remainder).find()) flag = false;
                    if (flag) entry.setPath(pathString);
                }
            }
        }

        // 3. remove apiDocEntries with duplicated paths
        Map<String, ApiDocEntry> updatedEntries = new LinkedHashMap<>();
        for (ApiDocEntry entry : apiDocEntries)
            updatedEntries.put(entry.getPath(), entry);
        apiDocEntries.clear();
        apiDocEntries.addAll(updatedEntries.values());
    }
}

/**
 * Classes to Create
 * 1. Token: {index}, {frequency}
 * 2. URL: {originalTokensList}, {normalizedTokensList}
 * 3. URLGroup: {length}, {pathSet}
 * - Normalization
 * - Construct URL objects with token index
 * - Count token frequency
 * - Update token frequency in the tokens of each URL
 * - Sort tokens of each URL based on freq and create normalizedTokensList
 * - Create URLGroups based on URL length
 * - Identification
 * -
 * - Reconstruction
 * - keep 2 sets; one for patterns with parameters, one for patterns without parameters
 * - Updating ApiDoc
 * for (entry : apiDocEntries)
 * if entry.path in patternsWithoutParams: pass
 * else:
 * entryPathTokens = entry.path.split('/').toSet()
 * for (pattern : patternsWithParams):
 * patternPathTokens = pattern.split('/).toSet()
 * if patternPathTokens.remove(entryPathTokens) == "{p}";
 * entry.path = pattern
 * - Duplicate elimination of ApiDocEntry based on path
 */
