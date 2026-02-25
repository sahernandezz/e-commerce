package com.challenge.ecommercebackend.modules.admin.persisten.repository.command;

import com.challenge.ecommercebackend.modules.admin.persisten.entity.HomepageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository de ESCRITURA (Command) para HomepageConfig
 * Conecta al DataSource de escritura (Master)
 * Utilizado para operaciones INSERT, UPDATE, DELETE
 */
@Repository
public interface IHomepageConfigCommandRepository extends JpaRepository<HomepageConfig, Long> {

}
