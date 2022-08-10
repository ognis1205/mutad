/*
 * Copyright 2021 Shingo OKAWA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ognis1205.mutad.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${application.description}") String desciption) {
        return new OpenAPI()
                .info(new Info()
                        .title("spring-boot-elasticsearch")
                        .version("")
                        .description("")
                        .termsOfService("")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}
