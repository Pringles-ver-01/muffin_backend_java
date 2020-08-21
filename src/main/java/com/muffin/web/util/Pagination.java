package com.muffin.web.util;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Data
@Component
@Lazy
public class Pagination {
    private int listSize, rangeSize, page, range, listCnt, pageCnt, startPage, startList, endPage;
    private boolean prev, next;

    public void pageInfo(int page, int range, int listCnt) {
        this.listSize = 10;
        this.rangeSize = 5;
        this.page = page;
        this.range = range;
        this.listCnt = listCnt;
        this.pageCnt = (listCnt % listSize != 0) ? (listCnt/listSize + 1) : (listCnt/listSize);
        this.startPage = (range-1) * rangeSize + 1;
        this.endPage = range * rangeSize;
        this.startList = (page - 1) * listSize;
        this.prev = range != 1;
        this.next = endPage <= pageCnt;

        if(this.endPage >= this.pageCnt){
            this.next = false;
        }
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getRangeSize() {
        return rangeSize;
    }

    public void setRangeSize(int rangeSize) {
        this.rangeSize = rangeSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getListCnt() {
        return listCnt;
    }

    public void setListCnt(int listCnt) {
        this.listCnt = listCnt;
    }

    public int getPageCnt() {
        return pageCnt;
    }

    public void setPageCnt(int pageCnt) {
        this.pageCnt = pageCnt;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getStartList() {
        return startList;
    }

    public void setStartList(int startList) {
        this.startList = startList;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isPrev() {
        return prev;
    }

    public void setPrev(boolean prev) {
        this.prev = prev;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }
}