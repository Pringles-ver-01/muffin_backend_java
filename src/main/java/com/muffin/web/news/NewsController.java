package com.muffin.web.news;

import com.muffin.web.util.Box;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private NewsService newsService;
    private Pagination pagination;

    @GetMapping("/pagination/{page}/{range}")
    public Map<?,?> pagination(@PathVariable int page, @PathVariable int range){
        System.out.println(page+" , "+range);
        pagination.pageInfo(page, range, newsService.count());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", newsService.pagination(pagination));
        return box;
    }

    @GetMapping("/csv")
    public void csvRead(){
        newsService.readCsv();
    }

    @GetMapping("/getList")
    public List<News> getNewsList(){
        System.out.println("컨트롤러");
        System.out.println(newsService.showNewsList());
        return newsService.showNewsList();
    }

    @GetMapping("/getDetail/{newsId}")
    public News getNewsDetail(@PathVariable Long newsId){
        return newsService.getNewsDetailById(newsId);
    }
}