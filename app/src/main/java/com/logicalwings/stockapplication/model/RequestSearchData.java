package com.logicalwings.stockapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestSearchData {
    @SerializedName("searchViewModelList")
    @Expose
    private List<AddTypeData> searchViewModelList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("pageIndex")
    @Expose
    private Integer pageIndex;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("sortType")
    @Expose
    private Integer sortType;
    @SerializedName("searchText")
    @Expose
    private String searchText;
    @SerializedName("sortField")
    @Expose
    private String sortField;

    public List<AddTypeData> getSearchViewModelList() {
        return searchViewModelList;
    }

    public void setSearchViewModelList(List<AddTypeData> searchViewModelList) {
        this.searchViewModelList = searchViewModelList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
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
}
