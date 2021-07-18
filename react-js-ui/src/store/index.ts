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
import { createStore, AnyAction, Store } from 'redux';
import { createWrapper, Context } from 'next-redux-wrapper';


export interface State {
    // Placeholder.
}

const reducer = (state: State = {}, action: AnyAction) => {
    switch (action.type) {
        default:
            return state;
    }
};

const makeStore = (context: Context) => createStore(reducer);

export const wrapper = createWrapper<Store<State>>(makeStore, {debug: true});
