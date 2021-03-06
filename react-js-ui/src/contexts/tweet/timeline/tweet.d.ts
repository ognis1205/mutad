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
export interface Model {
  user_id: string,
  user_name: string,
  image_url: string,
  text: string,
  hashtags: string[],
  timestamp: number,
}

export interface Query {
  before: number,
  text: string,
  hashtags: string[],
  page: number,
  size: number,
}

export interface Response {
  userId: string,
  userName: string,
  imageUrl: string,
  text: string,
  hashtags: string[],
  timestamp: number,
}
