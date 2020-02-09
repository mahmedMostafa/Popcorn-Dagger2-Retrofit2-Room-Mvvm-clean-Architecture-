package com.example.popcorn.models.response;

import com.example.popcorn.models.api.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesApiResponse {

    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    @SerializedName("total_results")
    @Expose
    private long totalResults;

    private List<Movie> results;

    public MoviesApiResponse() {
        this.results = new ArrayList<>();
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
