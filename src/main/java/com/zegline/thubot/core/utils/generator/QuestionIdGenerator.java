/**
 * @file QuestionIdGenerator.java
 * @brief Custom identifier generator for Hibernate entities
 *
 * This class implements a custom ID generator for entities in Hibernate. It generates unique identifiers
 * by combining a specified prefix with a random number. The uniqueness of the ID is ensured by checking
 * against existing entities in the database
 */
package com.zegline.thubot.core.utils.generator;

import java.io.Serializable;

import java.util.Properties;
import java.util.Random;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * @class QuestionIdGenerator
 * @brief Custom ID generator for Hibernate entities
 *
 * QuestionIdGenerator extends Hibernate's IdentifierGenerator to create unique IDs with a custom prefix
 * followed by a random number. It is used to generate IDs that do not collide with existing entities
 */
public class QuestionIdGenerator implements IdentifierGenerator {

    private String prefix;

    /**
     * Generates a unique identifier for an entity
     *
     * This method attempts to create a unique ID by appending a randomly generated number to a predefined prefix.
     * It repeats the process until it finds an ID that does not already exist in the database
     *
     * @param session The Hibernate session currently associated with the transaction
     * @param object The entity for which the ID is being generated
     * @return A unique identifier as a Serializable object
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String generatedId;

        do {
            int randomNum = new Random().nextInt(9000) + 1000;
            generatedId = prefix + randomNum;
        } while (isIdExists(session, generatedId));
        return generatedId;
    }

    /**
     * Checks the existence of a generated ID in the database
     *
     * This method queries the database to see if an ID already exists to ensure uniqueness of the generated IDs
     *
     * @param session The Hibernate session currently associated with the transaction
     * @param generatedId The ID to be checked
     * @return True if the ID exists, false otherwise
     */
    private boolean isIdExists(SharedSessionContractImplementor session, String generatedId) {
        // Check if an entity with the given ID already exists in the database
        return session.createQuery("select count(*) from DialogNode where id = :id", Long.class)
                .setParameter("id", generatedId)
                .uniqueResult() > 0;
    }

    /**
     * Configures the generator with necessary parameters
     *
     * This method configures the ID generator, setting the prefix that will be used when generating new IDs
     *
     * @param type The type of the entity for which the ID will be generated
     * @param params The parameters required for configuration, including the prefix
     * @param serviceRegistry The service registry
     * @throws MappingException If there is an issue with the configuration such as missing or invalid parameters
     */
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        setPrefix(params.getProperty("prefix")); 
    }

    /**
     * Sets the prefix to be used when generating new IDs
     *
     * @param p The prefix to be used in ID generation
     */
    public void setPrefix(String p) {
        prefix = p;
    }
}