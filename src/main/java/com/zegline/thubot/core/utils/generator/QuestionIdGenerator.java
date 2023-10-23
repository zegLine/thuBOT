package com.zegline.thubot.core.utils.generator;

import java.io.Serializable;
import java.util.Random;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class QuestionIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "Q";
        String generatedId;

        // Keep generating IDs until a unique one is found
        do {
            int randomNum = new Random().nextInt(9000) + 1000;
            generatedId = prefix + randomNum;
        } while (isIdExists(session, generatedId));

        return generatedId;
    }

    private boolean isIdExists(SharedSessionContractImplementor session, String generatedId) {
        // Check if an entity with the given ID already exists in the database
        return session.createQuery("select count(*) from Question where id = :id", Long.class)
                .setParameter("id", generatedId)
                .uniqueResult() > 0;
    }
}

