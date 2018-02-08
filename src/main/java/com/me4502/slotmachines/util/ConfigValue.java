/*
 * Copyright (c) Me4502 (Madeline Miller)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.me4502.slotmachines.util;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ConfigValue<T> {

    private String key;
    private String comment;
    private T defaultValue;
    private T value;

    private TypeToken<T> typeToken;

    public ConfigValue(String key, String comment, T value) {
        this(key, comment, value, null);
    }

    public ConfigValue(String key, String comment, T value, TypeToken<T> typeToken) {
        this.key = key;
        this.comment = comment;
        this.defaultValue = value;
        this.typeToken = typeToken;
    }

    public ConfigValue<T> load(ConfigurationNode configurationNode) {
        this.value = getValueInternal(configurationNode);
        save(configurationNode);
        return this;
    }

    private ConfigValue<T> save(ConfigurationNode configurationNode) {
        setValueInternal(configurationNode);
        return this;
    }

    public T getValue() {
        if (this.value == null) {
            this.value = this.defaultValue;
        }
        return this.value;
    }

    private void setValueInternal(ConfigurationNode configurationNode) {
        ConfigurationNode node = configurationNode.getNode(key);
        if(comment != null && node instanceof CommentedConfigurationNode) {
            ((CommentedConfigurationNode)node).setComment(comment);
        }

        if (typeToken != null) {
            try {
                node.setValue(typeToken, value);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        } else {
            node.setValue(value);
        }
    }

    private T getValueInternal(ConfigurationNode configurationNode) {
        ConfigurationNode node = configurationNode.getNode(key);

        if(comment != null && node instanceof CommentedConfigurationNode) {
            ((CommentedConfigurationNode)node).setComment(comment);
        }

        try {
            if(typeToken != null)
                return node.getValue(typeToken, defaultValue);
            else
                return node.getValue(new TypeToken<T>(defaultValue.getClass()){}, defaultValue);
        } catch(Exception e) {
            return defaultValue;
        }
    }
}
