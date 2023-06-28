package org.example.hw5.contexts.dispatching;

import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class EndpointPathTemplate {
    public EndpointPathTemplate(String method, String templateURI) {
        this.method = method;
        this.path = ridOfTrailingSlash(getPathFromTemplate(templateURI));
        this.queryParams = parseQueryParamsFromTemplate(templateURI);
    }

    public EndpointPathTemplate(String method, URI uri) {
        this.method = method;
        this.path = ridOfTrailingSlash(uri.getPath());
        this.queryParams = parseQueryParamsFromURI(uri);
    }

    private String ridOfTrailingSlash(String path) {
        return path.charAt(path.length() - 1) == '/'
                ? path.substring(0, path.length() - 1)
                : path;
    }

    private String getPathFromTemplate(String templateURI) {
        final var queryStartIndex = templateURI.indexOf('{');
        if(queryStartIndex == -1)
            return templateURI;
        return templateURI.substring(0, queryStartIndex - 1);
    }

    private Set<String> parseQueryParamsFromTemplate(String templateUri) {
        final var res = new TreeSet<String>();
        final var queryStartIndex = templateUri.indexOf('{');
        if(queryStartIndex == -1) return res;
        var query = templateUri.substring(queryStartIndex);
        if(query.charAt(0) != '{' || query.charAt(query.length() - 1) != '}') return res;
        query = query.substring(1, query.length() - 1);
        final var params = query.split(",");
        for(var param : params) {
            res.add(param.trim());
        }
        return res;
    }

    private Set<String> parseQueryParamsFromURI(URI uri) {
        final var query = uri.getQuery();
        if(query == null)
            return new TreeSet<>();
        return fromQuery(query);
    }

    private Set<String> fromQuery(String query) {
        final var res = new TreeSet<String>();
        var tokens = query.split("&");
        for (String token : tokens) {
            var params = token.split("=");
            if (params.length == 2) {
                res.add(params[0]);
            }
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EndpointPathTemplate other = (EndpointPathTemplate) obj;
        return Objects.equals(method, other.method) &&
                Objects.equals(path, other.path) &&
                Objects.equals(queryParams, other.queryParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path, queryParams);
    }

    private final String method;
    private final String path;
    private final Set<String> queryParams;
}
