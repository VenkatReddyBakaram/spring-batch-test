package com.vtechstorms.vems.service;

import com.vtechstorms.vems.entities.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class HibernateBatchItemWriter implements ItemWriter<Employee> {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 30;

    @Override
    @Transactional
    public void write(Chunk<? extends Employee> chunk) {
        int i = 0;
        for (Employee employee : chunk) {
            entityManager.persist(employee);
            i++;

            if (i % BATCH_SIZE == 0) {
                flushAndClear();
            }
        }

        // Ensure remaining items are flushed
        if (i % BATCH_SIZE != 0) {
            flushAndClear();
        }
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
