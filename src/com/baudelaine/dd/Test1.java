package com.baudelaine.dd;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Test1 {

	public static void main(String[] args) throws SQLException{
		// TODO Auto-generated method stub

//		long l0 = 30;
//		long l1 = 100;
//		long result = (Math.round(((double)l0 / l1) * 100));
//		System.out.println((int) result);
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		String schema = "DANONE";
		List<Object> result = new ArrayList<Object>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/lvso112", "danone", "danone");
			con.createStatement().execute("alter session set current_schema=" + schema);
			
			List<String> tables = Arrays.asList(
					"S_SAMPLE" 
					);
			
			List<String> queries = Arrays.asList(
					"select singular from systable join sdc on sdc.tableid = systable.tableid "
							+ "where systable.tableid = ?",
					"select tabledoc from systable join sdc on sdc.tableid = systable.tableid "
							+ "where systable.tableid = ?",
					"select tableid, columnid, columnlabel from syscolumn where tableid = ? and columnid in ('s_sampleid','sampledesc','sampletypeid','createdt','createby','createtool','moddt','modby','submitterid','modtool','submitteddt','auditdeferflag','notes','auditsequence','tracelogid','usersequence','reviewrequiredflag','priority','disposalstatus','samplestatus','collectiondt','receiveddt','physicalcondition','batchid','productid','projectid','samplepointid','locationid','requestid','starttestingdt','completedt','disposaldt','duedt','basedonsampleid','collectedby','templateflag','receivedby','disposedby','reviewedby','revieweddt','reviewdisposition','reviewremarks','numberlabels','controlsubstanceflag','receiverequiredflag','cocrequiredflag','eventdt','autoreceiveflag','autofinalrptflag','eventplan','studyid','conditionlabel','eventplanitem','schedulerulelabel','samplefamilyid','confirmedby','confirmeddt','sstudyid','pooledflag','glpflag','cocflag','restrictionsflag','concentration','concentrationunits','storagestatus','previousstoragestatus','deviations','treatments','allocatedforaddressid','allocatedforaddresstype','allocatedfordepartmentid','preptypeid','storagedisposalstatus','reagentlotid','qcsampletype','u_batch_info','u_source','u_sampling_stor_cond','u_samplecollecter','u_sampling_condition','u_product_info','u_pds_number','u_supplier','u_country','u_product_expireddt','u_europe','u_production_dt','u_packaging_type','u_amount','u_size_units','u_supply_dt','u_sampling_notes','u_remarks_coa','u_internal_remarks','u_sample_prep_remarks','u_planned_by','u_initial_by','u_completed_by','u_inprogress_by','u_closed_by','u_cancelled_by','u_preparation_status','u_start_testing_by','u_onhold_by','u_planned_date','u_closed_date','u_inprogress_date','u_onhold_date','u_initial_date','u_cancelled_date','u_laboratory','u_prep_required_flag','securityuser','securitydepartment','u_origine','u_sampling_dt','u_sampling_pack','u_start_testing_dt','u_onhold_remarks','u_review_remarks','u_dpt_location','u_samplequantity','u_expirationdt','u_sample_location','u_recipe_code','u_statuspreviewhold','u_localtime','u_number_client','u_productionstage','u_first_carton','u_eventid','u_samplingtime','u_conformity','u_excipiocaseid','u_expected_startdt','u_erp_number','u_storage_location','u_process_phase','specimentype','disposaltargetdt','sourcespid','sourcespversionid','sourcesplevelid','sourcespsourcelabel','batchstageid','activeflag','productversionid','instrumentid','workorderid','classification','u_reference1','u_batch1','u_supplier2','u_reference2','u_batch2','u_supplier3','u_reference3','u_batch3','u_palletid','u_studysiteid','u_subjectid','u_visitid','u_cali_status','u_phenotype','u_shipmentdt','u_returnddt','u_extlab_receivedt','u_production_site','u_cali_volume','u_toughed_time','u_mastersampleid')",
					"select tableid, columnid, columnlabel from syscolumn where tableid = ? and columnid in (?)",
					"select columndoc from syscolumn where tableid = ? and columnid in (?)"
					);
			
			for(String table: tables){
				
				Map<String, Object> tbl = new HashMap<String, Object>();
				tbl.put("table_name", table);

				stmt = con.prepareStatement(queries.get(0));
				stmt.setString(1, table.toLowerCase());
				rst = stmt.executeQuery ();
				while(rst.next()){
					tbl.put("table_remarks", rst.getString(1));
				}
				rst.close();
				stmt.close();
				
				stmt = con.prepareStatement(queries.get(1));
				stmt.setString(1, table.toLowerCase());
				rst = stmt.executeQuery ();
				while(rst.next()){
					tbl.put("table_description", rst.getString(1));
				}
				rst.close();
				stmt.close();
				
				DatabaseMetaData metaData = con.getMetaData();
				rst = metaData.getColumns(con.getCatalog(), schema, table, "%");
				List<String> fields = new ArrayList<String>();
				while(rst.next()){
					String column_name = rst.getString("COLUMN_NAME");
					fields.add("'" + column_name.toLowerCase() + "'");
				}
				
				List<Object> cols = new ArrayList<Object>();
				
				String in = "(" + StringUtils.join(fields.iterator(), ",") + ")";
				System.out.println("in=" + in);
				String qry  = StringUtils.replace(queries.get(3), "(?)", in);
				System.out.println(qry);
				
				stmt = con.prepareStatement(qry);
				stmt.setString(1, table.toLowerCase());
				rst = stmt.executeQuery();
				while(rst.next()){
					System.out.println(rst.getString(1) + "\t" + rst.getString(2) + "\t" + rst.getString(3));
				}
				rst.close();
				stmt.close();
				
				
//				while(rst.next()){
//					
//					Map<String, Object> col = new HashMap<String, Object>();
//					String column_name = rst.getString("COLUMN_NAME");
//					System.out.println("column_name=" + column_name);
//					col.put("column_name", column_name);
//					sb.append(",'" + column_name.toLowerCase() + "'");
					
//					stmt = con.prepareStatement(queries.get(2));
//					stmt.setString(1, table.toLowerCase());
//					stmt.setString(2, column_name.toLowerCase());
//					ResultSet subRst = stmt.executeQuery ();
//					while(subRst.next()){
//						System.out.println(subRst.getString(1));
//						col.put("column_remarks", subRst.getString(1));
//					}
//					subRst.close();
//					stmt.close();
//
//					stmt = con.prepareStatement(queries.get(3));
//					stmt.setString(1, table.toLowerCase());
//					stmt.setString(2, column_name.toLowerCase());
//					subRst = stmt.executeQuery ();
//					while(subRst.next()){
//						System.out.println(subRst.getString(1));
//						col.put("column_description", subRst.getString(1));
//					}
//					subRst.close();
//					stmt.close();
					
//					cols.add(col);
//				}
				
				rst.close();

				tbl.put("cols", cols);
				
				result.add(tbl);
	
			}

			System.out.println(result);
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(rst != null){
				rst.close();
			}
			if(stmt != null){
				stmt.close();
			}
			if(con != null){
				con.close();
			}
		}
		
	
	}

}
