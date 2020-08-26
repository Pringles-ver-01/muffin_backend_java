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
    private Box box;
    private Pagination pagination;

    @GetMapping("/search/{newsSearch}/{page}/{range}")
    public Map<?,?> searchNews(@PathVariable String newsSearch, @PathVariable int page, @PathVariable int range){
        System.out.println("news:"+newsSearch);
        System.out.println("news:"+page);
        System.out.println("news:"+range);
        pagination.pageInfo(page, range, newsService.findByNewsSearchWord(newsSearch).size());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", newsService.findByNewsSearchWordPage(newsSearch, pagination));
        return box;
    }


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
        return newsService.showNewsList();
    }

    @GetMapping("/getDetail/{newsId}")
    public News getNewsDetail(@PathVariable Long newsId){
        return newsService.getNewsDetailById(newsId);
    }
}