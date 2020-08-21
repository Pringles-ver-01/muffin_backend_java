package com.muffin.web.news;

import com.muffin.web.util.GenericService;
import com.muffin.web.util.Pagination;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

interface NewsService extends GenericService<News> {
    void readCsv();
    News getNewsDetailById(Long newsId);
    List<News> pagination(Pagination pagination);
    List<News> showNewsList();
    Optional<News> findById(Long id);
}

@Service
public class NewsServiceImpl implements NewsService{

    private final NewsRepository newsRepository;
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> pagination(Pagination pagination) {
        List<News> findNews = newsRepository.pagination(pagination);
        return findNews;
    }

    @Override
    public void readCsv() {
        InputStream is = getClass().getResourceAsStream("/static/news_crawling.csv");
        try {
            //Reader reader = Files.newBufferedReader(Paths.get("/static/news_crawling.csv"));
            //CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            //List<CSVRecord> records = readCSV(COMMENTS_HEADER, config.getCommentsCSV());
            //Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvParser){
                newsRepository.save(new News(
                        csvRecord.get(0),
                        csvRecord.get(1),
                        csvRecord.get(2),
                        csvRecord.get(3).replaceAll("(^\\p{Z}+|\\p{Z}+$)","\n"),
                        csvRecord.get(4)));
            };
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<News> showNewsList() {
        return newsRepository.showAllNews();
    }

    @Override
    public News getNewsDetailById(Long newsId) {
        return newsRepository.showNewsDetail(newsId);
    }


    @Override
    public Optional<News> findById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public Iterable<News> findAll() {
        return newsRepository.findAll();
    }

    @Override
    public int count() {
        return (int) newsRepository.count();
    }

    @Override
    public void delete(News news) {
        newsRepository.deleteAll();
    }

    @Override
    public boolean exists(String id) {
        return newsRepository.existsById(Long.valueOf(id));
    }
}