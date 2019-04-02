package com.logicalwings.stockapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchData {
    @SerializedName("List")
    @Expose
    private List<StockData> list = null;
    @SerializedName("GraphDataList")
    @Expose
    private Object graphDataList;
    @SerializedName("PageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("PageIndex")
    @Expose
    private Integer pageIndex;
    @SerializedName("TotalRows")
    @Expose
    private Integer totalRows;
    @SerializedName("TotalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("SearchText")
    @Expose
    private String searchText;
    @SerializedName("SortField")
    @Expose
    private String sortField;
    @SerializedName("SortType")
    @Expose
    private Integer sortType;

    public List<StockData> getList() {
        return list;
    }

    public void setList(List<StockData> list) {
        this.list = list;
    }

    public Object getGraphDataList() {
        return graphDataList;
    }

    public void setGraphDataList(Object graphDataList) {
        this.graphDataList = graphDataList;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }
}
