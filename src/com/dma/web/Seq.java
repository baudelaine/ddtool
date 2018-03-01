package com.dma.web;

public class Seq {

		String table_name = "";
		String pktable_name = "";
        String column_name = ""; 
        String pkcolumn_name = ""; 
        Short key_seq = null;

        public String getTable_name() {
			return table_name;
		}
		public void setTable_name(String table_name) {
			this.table_name = table_name;
		}
		public String getPktable_name() {
			return pktable_name;
		}
		public void setPktable_name(String pktable_name) {
			this.pktable_name = pktable_name;
		}
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
