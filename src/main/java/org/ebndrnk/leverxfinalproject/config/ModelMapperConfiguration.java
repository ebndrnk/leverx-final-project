package org.ebndrnk.leverxfinalproject.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    /**
     * Creates another ModelMapper bean.
     *
     * @return a new ModelMapper instance
     */
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
