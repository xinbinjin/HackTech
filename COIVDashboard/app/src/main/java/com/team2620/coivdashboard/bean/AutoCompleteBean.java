package com.team2620.coivdashboard.bean;

import java.util.List;

public class AutoCompleteBean {
    /**
     * predictions : [{"description":"Beverly Hills, CA, USA","id":"977701bc4a223d0742b11b2dbb39895ef4541a08","matched_substrings":[{"length":2,"offset":0}],"place_id":"ChIJq0fR1gS8woAR0R4I_XnDx9Y","reference":"ChIJq0fR1gS8woAR0R4I_XnDx9Y","structured_formatting":{"main_text":"Beverly Hills","main_text_matched_substrings":[{"length":2,"offset":0}],"secondary_text":"CA, USA"},"terms":[{"offset":0,"value":"Beverly Hills"},{"offset":15,"value":"CA"},{"offset":19,"value":"USA"}],"types":["locality","political","geocode"]},{"description":"Bellflower, CA, USA","id":"caaba7aa3e5489e99bb9683c006734eeb70491d0","matched_substrings":[{"length":2,"offset":0}],"place_id":"ChIJc0O-9bAy3YARCZtI9k9VBrY","reference":"ChIJc0O-9bAy3YARCZtI9k9VBrY","structured_formatting":{"main_text":"Bellflower","main_text_matched_substrings":[{"length":2,"offset":0}],"secondary_text":"CA, USA"},"terms":[{"offset":0,"value":"Bellflower"},{"offset":12,"value":"CA"},{"offset":16,"value":"USA"}],"types":["locality","political","geocode"]},{"description":"Bell Gardens, CA, USA","id":"2171e20520830b0ee2bff627421a619acb2ca455","matched_substrings":[{"length":2,"offset":0}],"place_id":"ChIJFZG_A9bNwoARm9_9Cfsrat4","reference":"ChIJFZG_A9bNwoARm9_9Cfsrat4","structured_formatting":{"main_text":"Bell Gardens","main_text_matched_substrings":[{"length":2,"offset":0}],"secondary_text":"CA, USA"},"terms":[{"offset":0,"value":"Bell Gardens"},{"offset":14,"value":"CA"},{"offset":18,"value":"USA"}],"types":["locality","political","geocode"]},{"description":"Bell, CA, USA","id":"a90208b9a899638c45e94f1a8db6266109c15098","matched_substrings":[{"length":2,"offset":0}],"place_id":"ChIJkW_58pTOwoARGiQ2VqD0rhA","reference":"ChIJkW_58pTOwoARGiQ2VqD0rhA","structured_formatting":{"main_text":"Bell","main_text_matched_substrings":[{"length":2,"offset":0}],"secondary_text":"CA, USA"},"terms":[{"offset":0,"value":"Bell"},{"offset":6,"value":"CA"},{"offset":10,"value":"USA"}],"types":["locality","political","geocode"]},{"description":"Beaumont, CA, USA","id":"d80ef5f165e7da97c659b666cac9c5b336dbc5e8","matched_substrings":[{"length":2,"offset":0}],"place_id":"ChIJb-EVG65E24ARyxp9OMqnDCM","reference":"ChIJb-EVG65E24ARyxp9OMqnDCM","structured_formatting":{"main_text":"Beaumont","main_text_matched_substrings":[{"length":2,"offset":0}],"secondary_text":"CA, USA"},"terms":[{"offset":0,"value":"Beaumont"},{"offset":10,"value":"CA"},{"offset":14,"value":"USA"}],"types":["locality","political","geocode"]}]
     * status : OK
     */

    private String status;
    private List<PredictionsBean> predictions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PredictionsBean> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionsBean> predictions) {
        this.predictions = predictions;
    }

    public static class PredictionsBean {
        /**
         * description : Beverly Hills, CA, USA
         * id : 977701bc4a223d0742b11b2dbb39895ef4541a08
         * matched_substrings : [{"length":2,"offset":0}]
         * place_id : ChIJq0fR1gS8woAR0R4I_XnDx9Y
         * reference : ChIJq0fR1gS8woAR0R4I_XnDx9Y
         * structured_formatting : {"main_text":"Beverly Hills","main_text_matched_substrings":[{"length":2,"offset":0}],"secondary_text":"CA, USA"}
         * terms : [{"offset":0,"value":"Beverly Hills"},{"offset":15,"value":"CA"},{"offset":19,"value":"USA"}]
         * types : ["locality","political","geocode"]
         */

        private String description;
        private String id;
        private String place_id;
        private String reference;
        private StructuredFormattingBean structured_formatting;
        private List<MatchedSubstringsBean> matched_substrings;
        private List<TermsBean> terms;
        private List<String> types;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public StructuredFormattingBean getStructured_formatting() {
            return structured_formatting;
        }

        public void setStructured_formatting(StructuredFormattingBean structured_formatting) {
            this.structured_formatting = structured_formatting;
        }

        public List<MatchedSubstringsBean> getMatched_substrings() {
            return matched_substrings;
        }

        public void setMatched_substrings(List<MatchedSubstringsBean> matched_substrings) {
            this.matched_substrings = matched_substrings;
        }

        public List<TermsBean> getTerms() {
            return terms;
        }

        public void setTerms(List<TermsBean> terms) {
            this.terms = terms;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class StructuredFormattingBean {
            /**
             * main_text : Beverly Hills
             * main_text_matched_substrings : [{"length":2,"offset":0}]
             * secondary_text : CA, USA
             */

            private String main_text;
            private String secondary_text;
            private List<MainTextMatchedSubstringsBean> main_text_matched_substrings;

            public String getMain_text() {
                return main_text;
            }

            public void setMain_text(String main_text) {
                this.main_text = main_text;
            }

            public String getSecondary_text() {
                return secondary_text;
            }

            public void setSecondary_text(String secondary_text) {
                this.secondary_text = secondary_text;
            }

            public List<MainTextMatchedSubstringsBean> getMain_text_matched_substrings() {
                return main_text_matched_substrings;
            }

            public void setMain_text_matched_substrings(List<MainTextMatchedSubstringsBean> main_text_matched_substrings) {
                this.main_text_matched_substrings = main_text_matched_substrings;
            }

            public static class MainTextMatchedSubstringsBean {
                /**
                 * length : 2
                 * offset : 0
                 */

                private int length;
                private int offset;

                public int getLength() {
                    return length;
                }

                public void setLength(int length) {
                    this.length = length;
                }

                public int getOffset() {
                    return offset;
                }

                public void setOffset(int offset) {
                    this.offset = offset;
                }
            }
        }

        public static class MatchedSubstringsBean {
            /**
             * length : 2
             * offset : 0
             */

            private int length;
            private int offset;

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }
        }

        public static class TermsBean {
            /**
             * offset : 0
             * value : Beverly Hills
             */

            private int offset;
            private String value;

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
