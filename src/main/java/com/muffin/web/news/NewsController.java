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

    @GetMapping("/search/{searchWord}/{page}/{range}")
    public Map<?, ?> search(@PathVariable String searchWord, @PathVariable int page, @PathVariable int range){
        System.out.println(searchWord);
        pagination.pageInfo(page, range, newsService.findBySearchWord(searchWord).size());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", newsService.findBySearchWordPage(searchWord, pagination));
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
        System.out.println(newsId);
        return newsService.getNewsDetailById(newsId);
    }
}
