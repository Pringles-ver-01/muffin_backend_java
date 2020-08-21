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
        this.pageCnt = (listCnt % listSize != 0) ? (listCnt/listSize + 1) : (listCnt/listSize);;
        this.startPage = (range-1) * rangeSize + 1;
        this.endPage = range * rangeSize;
        this.startList = (page - 1) * listSize;
        this.prev = range != 1;
        this.next = endPage <= pageCnt;
        if(this.endPage > this.pageCnt){
            this.endPage = this.pageCnt;
        }
    }
}
