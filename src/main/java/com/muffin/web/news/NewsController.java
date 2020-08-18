package com.muffin.web.news;

import com.muffin.web.util.Box;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private Pagination pagination;
    private NewsService newsService;
    private Box box;
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @GetMapping("/csv")
    public void csvRead(){
        newsService.readCsv();
    }

    @GetMapping("/getList")
    public List<News> getNewsList(){
        System.out.println("컨트롤러");
        return newsService.showNewsList();
    }

    @GetMapping("/getDetail/{newsId}")
    public News getNewsDetail(@PathVariable Long newsId){
        System.out.println(newsId);
        System.out.println(newsService.getNewsDetailById(newsId));
        return newsService.getNewsDetailById(newsId);
    }

    /*@GetMapping("/list")
    public HashMap<String, ?> pageList(@PathVariable String currentPage){
        pagination.pageInfo(newsService.count(), Integer.parseInt(currentPage));
        List<News> list = newsService.pageInfoList(pagination);
        box.clear();
        box.put("pagination", pagination);
        box.put("list", list);
        System.out.println(box.get());
        return box.get();
    }*/
}
