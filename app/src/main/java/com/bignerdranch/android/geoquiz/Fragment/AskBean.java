package com.bignerdranch.android.geoquiz.Fragment;

public class AskBean {

        /**
         * details : 今天在武汉 ，阳关明媚
         * title : 明天天气怎么样
         */

        private String details;
        private String title;

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    @Override
    public String toString() {
        return "AskBean{" +
                "details='" + details + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
