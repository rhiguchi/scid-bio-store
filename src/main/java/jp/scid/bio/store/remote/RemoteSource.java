package jp.scid.bio.store.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RemoteSource {
    private final static Logger logger = Logger.getLogger(RemoteSource.class.getName());
    
    private enum Command {
        SEARCH, ENTRY;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    private enum EntryField {
        IDENTIFIER, ACCESSION, LENGTH, DEFINITION, TAXONOMY;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private final URI togowsBaseUri = URI.create("http://togows.dbcls.jp");
    private final HttpClient httpclient = new DefaultHttpClient();
    private int oneQueryLimit = 10;
    
    int retrieveCount(String string) throws IOException {
        String path = getCountQueryPath(string);
        String content = retrieveContentFromTogows(path);
        
        return perseInt(content);
    }

    List<String> searchIdentifiers(String query, int offset, int limit) throws IOException {
        String path = getSearchQueryPath(query, offset, limit);
        String[] identifiers = retrieveLinesFromTogows(path);
        return Arrays.asList(identifiers);
    }
    
    List<RemoteEntry> searchEntry(List<String> identifiers) throws IOException {
        if (identifiers == null || identifiers.size() == 0) {
            throw new IllegalArgumentException("identifiers must not be empty");
        }
        
        String identifiersString = join(identifiers);
        String[] definitions = retrieveField(EntryField.DEFINITION, identifiersString);
        String[] accession = retrieveField(EntryField.ACCESSION, identifiersString);
        String[] taxonomy = retrieveField(EntryField.TAXONOMY, identifiersString);
        int[] length = parseInt(retrieveField(EntryField.LENGTH, identifiersString));
        
        List<RemoteEntry> entries = new ArrayList<RemoteEntry>(identifiers.size());
        RemoteEntryBuilder builder = new RemoteEntryBuilder();
        
        for (int i = 0; i < identifiers.size(); i++) {
            builder.identifier(identifiers.get(i));
            builder.accession(accession[i]);
            builder.definition(definitions[i]);
            builder.sequenceLength(length[i]);
            builder.taxonomy(taxonomy[i]);
            
            entries.add(builder.build());
        }
        
        return entries;
    }

    private String[] retrieveField(EntryField definition, String query)
            throws IOException, ClientProtocolException {
        String definitionPath = getQueryPathString(Command.ENTRY, query, definition.toString());
        return retrieveLinesFromTogows(definitionPath);
    }


    private URI createQueryUri(String path) {
        URIBuilder builder = new URIBuilder(togowsBaseUri);
        builder.setPath(path);
        
        URI queryUri;
        try {
            queryUri = builder.build();
        }
        catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        return queryUri;
    }

    private String retrieveContentFromTogows(String path)
            throws IOException, ClientProtocolException {
        URI queryUri = createQueryUri(path);
        
        return retrieve(queryUri);
    }

    private String[] retrieveLinesFromTogows(String path) throws IOException, ClientProtocolException {
        return retrieveContentFromTogows(path).split("\n");
    }
    
    private String retrieve(URI resource) throws IOException, ClientProtocolException {
        HttpGet request = new HttpGet(resource);
        logger.fine(request.getMethod() + ": " + request.getURI());
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        return content;
    }

    private String getCountQueryPath(String string) {
        return getQueryPathString(Command.SEARCH, urlEncode(string), "count");
    }

    private String getSearchQueryPath(String string, int offset, int limit) {
        StringBuilder option = new StringBuilder();
        option.append(offset).append(",").append(limit);
        
        return getQueryPathString(Command.SEARCH, urlEncode(string), option);
    }
    
    private static String join(List<String> identifiers) {
        StringBuilder queryBuilder = new StringBuilder();
        for (String identifier: identifiers) {
            queryBuilder.append(identifier).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        return queryBuilder.toString();
    }

    private String getQueryPathString(Command command, String query, CharSequence option) {
        if (query == null || query.isEmpty())
            throw new IllegalArgumentException("string must not be empty");
        
        StringBuilder pathBuilder = new StringBuilder("/");
        pathBuilder.append(command).append("/nucleotide/");
        pathBuilder.append(query).append("/").append(option);
        
        return pathBuilder.toString();
    }

    // ~~~~
    // Utils
    // ~~~~
    private static int perseInt(String content) throws IOException {
        try {
            return Integer.parseInt(content);
        }
        catch (NumberFormatException e) {
            throw new IOException("non-number response", e);
        }
    }
    
    private static int[] parseInt(String... strings) throws IOException {
        int[] values = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            values[i] = perseInt(string);
        }
        return values;
    }

    private static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static class RemoteEntryBuilder {
        private String identifier = "";
        private String accession = "";
        private int sequenceLength = 0;
        private String definition = "";
        private String taxonomy = "";
        
        public RemoteEntry build() {
            return new RemoteEntry(this);
        }
        
        public void identifier(String identifier) {
            this.identifier = identifier;
        }
        
        public void accession(String accession) {
            this.accession = accession;
        }
        
        public void sequenceLength(int sequenceLength) {
            this.sequenceLength = sequenceLength;
        }
        
        public void definition(String definition) {
            this.definition = definition;
        }
        
        public void taxonomy(String taxonomy) {
            this.taxonomy = taxonomy;
        }
    }
    
    public static class RemoteEntry {
        private final String identifier;
        private final String accession;
        private final int sequenceLength;
        private final String definition;
        private final String taxonomy;
        
        RemoteEntry(RemoteEntryBuilder builder) {
            this.identifier = builder.identifier;
            this.accession = builder.accession;
            this.sequenceLength = builder.sequenceLength;
            this.definition = builder.definition;
            this.taxonomy = builder.taxonomy;
        }
        
        public String identifier() {
            return identifier;
        }
        
        public String accession() {
            return accession;
        }
        
        public int sequenceLength() {
            return sequenceLength;
        }
        
        public String definition() {
            return definition;
        }
        
        public String taxonomy() {
            return taxonomy;
        }
    }
    
    private Callable<Result> createSearchTask(String query, int limit, ResultHandler resultHandler) throws IOException {
        AllEntriesSearchingTask task = new AllEntriesSearchingTask(query, resultHandler);
        
        int pageLimit = oneQueryLimit;
        for (int offset = 1; offset <= limit; offset += pageLimit) {
            PageEntriesSearchingTask pageTask =
                    new PageEntriesSearchingTask(query, offset, pageLimit);
            task.addTask(pageTask);
        }
        
        return task;
    }
    
    public Callable<Searcher> createSearchTask(final String query, final ResultHandler resultHandler) {
        return new Callable<Searcher>() {
            @Override
            public Searcher call() throws Exception {
                int count = retrieveCount(query);
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                
                Callable<Result> resultTask = createSearchTask(query, count, resultHandler);
                return new Searcher(count, resultTask);
            }
        };
    }
    
    public static interface ResultHandler {
        void process(List<RemoteEntry> results);
    }
    
    public static class Searcher implements Callable<Result> {
        private int count;
        private Callable<Result> task;
        
        private Searcher(int count, Callable<Result> task) {
            this.count = count;
            this.task = task;
        }

        public int getCount() {
            return count;
        }
        
        @Override
        public Result call() throws Exception {
            return task.call();
        }
    }
    
    class PageEntriesSearchingTask implements Callable<List<RemoteEntry>> {
        private final String query;
        private final int offset;
        private final int limit;
        
        public PageEntriesSearchingTask(String query, int offset, int limit) {
            this.query = query;
            this.offset = offset;
            this.limit = limit;
        }

        @Override
        public List<RemoteEntry> call() throws Exception {
            List<String> identifiers = searchIdentifiers(query, offset, limit);
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("page");
            }
            return searchEntry(identifiers);
        }
    }
    
    class AllEntriesSearchingTask implements Callable<Result> {
        private Queue<PageEntriesSearchingTask> pageTasks;

        private final ResultBuilder builder;
        private final ResultHandler handler;
        
        public AllEntriesSearchingTask(String query, ResultHandler handler) {
            this.pageTasks = new LinkedList<PageEntriesSearchingTask>();
            builder = new ResultBuilder(query);
            this.handler = handler;
        }

        public boolean addTask(PageEntriesSearchingTask e) {
            return pageTasks.add(e);
        }
        
        @Override
        public Result call() throws Exception {
            while (!pageTasks.isEmpty()) {
                PageEntriesSearchingTask t = pageTasks.remove();
                
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Searching is Cancelled");
                }
                
                List<RemoteEntry> list = t.call();
                builder.addEntries(list);
                
                handler.process(list);
            }
            
            return builder.build();
        }
    }
    
    public static class Result {
        private final String queryString;
        private final List<RemoteEntry> entries;
        
        private Result(ResultBuilder builder) {
            this.queryString = builder.queryString;
            entries = Collections.unmodifiableList(new ArrayList<RemoteEntry>(builder.entries));
        }
        
        public String queryString() {
            return queryString;
        }
        
        public int count() {
            return entries.size();
        }
        
        public List<RemoteEntry> getEntries() {
            return entries;
        }
    }
    
    private static class ResultBuilder {
        private String queryString = "";
        private final List<RemoteEntry> entries;

        public ResultBuilder(String queryString) {
            this.queryString = queryString;
            entries = new LinkedList<RemoteEntry>();
        }
        
        public Result build() {
            return new Result(this);
        }
        
        public boolean addEntries(Collection<RemoteEntry> entries) {
            return entries.addAll(entries);
        }
    }
}
