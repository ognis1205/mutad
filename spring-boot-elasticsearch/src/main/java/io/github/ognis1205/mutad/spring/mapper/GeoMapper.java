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
import io.github.ognis1205.mutad.spring.dto.GeoDTO;
import io.github.ognis1205.mutad.spring.model.Geo;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Mapper
public interface GeoMapper {
    /** Singleton instance. */
    public GeoMapper INSTANCE = Mappers.getMapper(GeoMapper.class);

    /** Converts `Geo` into `GeoDTO`. */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "lang", source = "lang")
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "hashtags", source = "hashtags")
    @Mapping(target = "cityName", source = "cityName")
    @Mapping(target = "cityCoord", source = "cityCoord")
    public GeoDTO convert(Geo geo);

    /** Converts `Geo` into `GeoDTO`. */
    public List<GeoDTO> convert(List<Geo> geos);
}

