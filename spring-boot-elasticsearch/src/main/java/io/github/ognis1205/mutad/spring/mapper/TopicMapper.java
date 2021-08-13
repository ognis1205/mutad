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
package io.github.ognis1205.mutad.spring.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.github.ognis1205.mutad.spring.dto.TopicDTO;
import io.github.ognis1205.mutad.spring.model.Topic;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Mapper
public interface TopicMapper {
    /** Singleton instance. */
    public TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    /** Converts `Topic` into `TopicDTO`. */
    @Mapping(target = "name", source = "name")
    @Mapping(target = "count", source = "count")
    public TopicDTO convert(Topic topic);

    /** Converts `Topic` into `TopicDTO`. */
    public List<TopicDTO> convert(List<Topic> topics);
}
