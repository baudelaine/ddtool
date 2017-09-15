package com.baudelaine.dd;

public class Seq {

        String column_name = ""; 
        String pkcolumn_name = ""; 
        Short key_seq = null;

        public String getColumn_name() {
                return column_name;
        }
        public void setColumn_name(String column_name) {
                this.column_name = column_name;
        }
        public String getPkcolumn_name() {
                return pkcolumn_name;
        }
        public void setPkcolumn_name(String pkcolumn_name) {
                this.pkcolumn_name = pkcolumn_name;
        }
        public Short getKey_seq() {
                return key_seq;
        }
        public void setKey_seq(Short key_seq) {
                this.key_seq = key_seq;
        }
    
}
