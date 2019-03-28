package ru.otus.spring02.migration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring02.model.Book;
import ru.otus.spring02.model.MongoBook;
import ru.otus.spring02.repository.MongoBookRepository;

import javax.persistence.EntityManagerFactory;

@EnableBatchProcessing
@Configuration
public class NoSqlMigrationConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private NoSqlMapper mapper;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @Bean
    public JpaPagingItemReader<Book> bookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(30)
                .queryString("select b from Book b")
                .build();
    }

    @Bean
    public ItemProcessor<Book, MongoBook> bookProcessor() {
        return mapper::mapBookToMongo;
    }

    @Bean
    public RepositoryItemWriter<MongoBook> mongoBookWriter() {
        return new RepositoryItemWriterBuilder<MongoBook>()
                .repository(mongoBookRepository).methodName("save")
                .build();
    }

    @Bean
    public Step step1(JpaPagingItemReader<Book> bookReader, RepositoryItemWriter<MongoBook> mongoBookWriter, ItemProcessor bookProcessor) {
        return stepBuilderFactory.get("step1")
                .chunk(30)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(mongoBookWriter)
                .build();
    }

    @Bean
    public Job noSqlMigrationJob(Step step1) {
        return jobBuilderFactory.get("noSqlMigrationJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }
}
