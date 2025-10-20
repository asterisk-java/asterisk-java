/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.action.api;

import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.api.response.DefaultActionResponse;

import java.io.Serial;
import java.util.List;

import static org.asteriskjava.ami.action.api.UpdateConfigAction.Config.Action.*;
import static org.asteriskjava.core.databind.utils.AsteriskBoolean.fromBoolean;

/**
 * Update basic configuration.
 * <p>
 * This action will modify, create, or delete configuration elements in Asterisk configuration files.
 * <p>
 * Please take note that unlike the manager documentation, this command does not dump back the config file upon success.
 * It only tells you it succeeded.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/UpdateConfig/">UpdateConfig</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/UpdateConfig/">UpdateConfig</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @author Steve Prior
 * @author Martin Smith
 * @author Piotr Olaszewski
 * @since 1.0.0
 */
@ExpectedResponse(DefaultActionResponse.class)
public class UpdateConfigAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = 4753117770471622025L;

    private String srcFilename;
    private String dstFilename;
    private String reload;
    private boolean preserveEffectiveContext = true;
    private List<Config> configs;

    @Override
    public String getAction() {
        return "UpdateConfig";
    }

    public String getSrcFilename() {
        return srcFilename;
    }

    public void setSrcFilename(String srcFilename) {
        this.srcFilename = srcFilename;
    }

    public String getDstFilename() {
        return dstFilename;
    }

    public void setDstFilename(String dstFilename) {
        this.dstFilename = dstFilename;
    }

    public String getReload() {
        return reload;
    }

    public void setReload(String reloadModule) {
        this.reload = reloadModule;
    }

    public void setReload(boolean reload) {
        this.reload = fromBoolean(reload);
    }

    public boolean isPreserveEffectiveContext() {
        return preserveEffectiveContext;
    }

    public UpdateConfigAction setPreserveEffectiveContext(boolean preserveEffectiveContext) {
        this.preserveEffectiveContext = preserveEffectiveContext;
        return this;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public abstract static class Config {
        public enum Action {
            NewCat,
            RenameCat,
            DelCat,
            EmptyCat,
            Update,
            Delete,
            Append,
            Insert,
        }

        private String cat;
        private String options;

        public abstract Action getAction();

        public String getCat() {
            return cat;
        }

        public void setCat(String cat) {
            this.cat = cat;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }
    }

    public static class NewCat extends Config {
        @Override
        public Config.Action getAction() {
            return NewCat;
        }
    }

    public static class RenameCat extends Config {
        private String value;

        @Override
        public Config.Action getAction() {
            return RenameCat;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class DelCat extends Config {
        @Override
        public Config.Action getAction() {
            return DelCat;
        }
    }

    public static class EmptyCat extends Config {
        @Override
        public Config.Action getAction() {
            return EmptyCat;
        }
    }

    public static class Update extends Config {
        private String var;
        private String value;
        private String match;

        @Override
        public Config.Action getAction() {
            return Update;
        }

        public String getVar() {
            return var;
        }

        public void setVar(String var) {
            this.var = var;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMatch() {
            return match;
        }

        public void setMatch(String match) {
            this.match = match;
        }
    }

    public static class Delete extends Config {
        private String var;
        private Integer line;
        private String match;

        @Override
        public Config.Action getAction() {
            return Delete;
        }

        public String getVar() {
            return var;
        }

        public void setVar(String var) {
            this.var = var;
        }

        public Integer getLine() {
            return line;
        }

        public void setLine(Integer line) {
            this.line = line;
        }

        public String getMatch() {
            return match;
        }

        public void setMatch(String match) {
            this.match = match;
        }
    }

    public static class Append extends Config {
        private String var;
        private String value;

        @Override
        public Config.Action getAction() {
            return Append;
        }

        public String getVar() {
            return var;
        }

        public void setVar(String var) {
            this.var = var;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Insert extends Config {
        private String var;
        private int line;
        private String value;

        @Override
        public Config.Action getAction() {
            return Insert;
        }

        public String getVar() {
            return var;
        }

        public void setVar(String var) {
            this.var = var;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
