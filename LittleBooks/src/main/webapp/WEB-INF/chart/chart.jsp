<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
   String ctxPath = request.getContextPath(); 
        // MyMVC
%>

<jsp:include page="../header1.jsp" />

<style type="text/css">
.highcharts-figure,
.highcharts-data-table table {
    min-width: 320px;
    max-width: 800px;
    margin: 1em auto;
}

.highcharts-data-table table {
    font-family: Verdana, sans-serif;
    border-collapse: collapse;
    border: 1px solid #ebebeb;
    margin: 10px auto;
    text-align: center;
    width: 100%;
    max-width: 500px;
}

.highcharts-data-table caption {
    padding: 1em 0;
    font-size: 1.2em;
    color: #555;
}

.highcharts-data-table th {
    font-weight: 600;
    padding: 0.5em;
}

.highcharts-data-table td,
.highcharts-data-table th,
.highcharts-data-table caption {
    padding: 0.5em;
}

.highcharts-data-table thead tr,
.highcharts-data-table tr:nth-child(even) {
    background: #f8f8f8;
}

.highcharts-data-table tr:hover {
    background: #f1f7ff;
}

input[type="number"] {
    min-width: 50px;
}

div#table_container table {width: 100%}
div#table_container th, div#table_container td {border: solid 1px gray; text-align: center;} 
div#table_container th {background-color: #595959; color: white;}

</style>

<script src="<%= ctxPath%>/Highcharts-10.3.1/code/highcharts.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/exporting.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/export-data.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/accessibility.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/series-label.js"></script>

<div style="display: flex;">   
 <div style="width: 80%; min-height: 1100px; margin:auto; ">

   <h2 style="margin: 50px 0;">${sessionScope.loginuser.name} 님의 주문통계 차트</h2>
   
   <form name="searchFrm" style="margin: 20px 0 50px 0; ">
      <select name="searchType" id="searchType" style="height: 40px;">
         <option value="">통계선택하세요</option>
         <option value="myPurchase_byCategory" selected>나의 카테고리별주문 통계</option>
         <option value="myPurchase_byMonth_byCategory">나의 카테고리별 월별주문 통계</option>
      </select>
   </form>
   
   <div id="chart_container"></div>
   <div id="table_container" style="margin: 40px 0 0 0;"></div>
	
 </div>
</div>
 
 
<script type="text/javascript">

  $(function(){
	  
	  $('select#searchType').change(function(e){
		  func_choice($(e.target).val());
		// $(e.target).val() 은 "" 또는 "myPurchase_byCategory" 또는 "myPurchase_byMonth_byCategory" 이다. 
	  });
	  func_choice('myPurchase_byCategory');
  });// end of $(function(){})----------------------


  // Function Declaration 
  function func_choice(searchTypeVal) {
	  
	// alert(searchTypeVal);
	
	switch (searchTypeVal) {
	      case "": // "통계선택하세요" 를 선택한 경우
	    	  $("div#chart_container").empty();
	          $("div#table_container").empty();
	      
		     break;
	  
	      case "myPurchase_byCategory": // "나의 카테고리별주문 통계" 를 선택한 경우
			
	          $.ajax({
	        	  url:"<%= ctxPath%>/shop/myPurchase_byCategoryJSON.go",
	        	  data:{"userid":"${sessionScope.loginuser.userid}"},
				  dataType:"json",
	        	  success:function(json){
	        		 // console.log(JSON.stringify(json));
	        		 /*
	        		    [{"categoryname":"전자제품", "cnt":"1", "sumpay":"1000000", "sumpay_pct":"66.05"}
	        		    ,{"categoryname":"도서",    "cnt":"1", "sumpay":"297000",  "sumpay_pct":"19.62"}
	        		    ,{"categoryname":"의류",    "cnt":"3", "sumpay":"217000",  "sumpay_pct":"14.33"}]
	        		 */
	        		  	  
	        		  $("div#chart_container").empty(); 
	        		 
	        		  let resultArr = [];
	        		 
	        		  for(let i=0; i<json.length; i++){
	        			  
	        			  let obj;
	        			  
	        			  if(i==0){
	        				  obj = {name: json[i].categoryname,
			    		             y: Number(json[i].sumpay_pct),
			    		             sliced: true,
			    		             selected: true};
	        			  }
	        			  else {
	        				  obj = {name: json[i].categoryname,
			    		             y: Number(json[i].sumpay_pct)};
	        			  }
	        			  
	        			  resultArr.push(obj); // 배열속에 객체를 넣기
	        		  }// end of for-------------------------------------
	        		  
                      //////////////////////////////////////////////////////////////////
	    	    	  Highcharts.chart('chart_container', {
	    	    		    chart: {
	    	    		        plotBackgroundColor: null,
	    	    		        plotBorderWidth: null,
	    	    		        plotShadow: false,
	    	    		        type: 'pie'
	    	    		    },
	    	    		    title: {
	    	    		        text: '나의 주문 통계'
	    	    		    },
	    	    		    tooltip: {
	    	    		        pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
	    	    		    },
	    	    		    accessibility: {
	    	    		        point: {
	    	    		            valueSuffix: '%'
	    	    		        }
	    	    		    },
	    	    		    plotOptions: {
	    	    		        pie: {
	    	    		            allowPointSelect: true,
	    	    		            cursor: 'pointer',
	    	    		            dataLabels: {
	    	    		                enabled: true,
	    	    		                format: '<b>{point.name}</b>: {point.percentage:.2f} %'
	    	    		            }
	    	    		        }
	    	    		    },
	    	    		    series: [{
	    	    		        name: '주문금액비율',
	    	    		        colorByPoint: true,
	    	    		        data: resultArr
	    	    		    }]
	    	    		});
	        		  
	        	  //////////////////////////////////////////////////
	    	    	  let html = "<table>";
	                   html += "<tr>" +
	                           "<th>카테고리</th>" +
	                           "<th>주문수</th>" +
	                           "<th>총주문금액</th>" +
	                           "<th>퍼센티지</th>" +
	                         "</tr>";
	      
	                $.each(json, function(index, item){
	                      html += "<tr>" +
	                                  "<td>"+ item.categoryname +"</td>" +
	                                  "<td>"+ item.cnt +"</td>" +
	                                  "<td style='text-align: right;'>"+ Number(item.sumpay).toLocaleString('en') +" 원</td>" +
	                                  "<td>"+ Number(item.sumpay_pct) +" %</td>" +
	                              "</tr>";
	                });        
	                           
	                html += "</table>";
	    
	                   $("div#table_container").html(html);
	        		  
	        	  },
	        	  error: function(request, status, error){
					 alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				  }
	          });
	      
			 break;
	
			 
	      case "myPurchase_byMonth_byCategory": // "나의 카테고리별 월별주문 통계" 를 선택한 경우
			
	    	  $.ajax({
					url:"<%= ctxPath%>/shop/myPurchase_byMonth_byCategoryJSON.go",
					data:{"userid":"${sessionScope.loginuser.userid}"},
					dataType:"json",
					success:function(json){
					    
					 // console.log(JSON.stringify(json));
					 
					     $("div#chart_container").empty();
					  
					     const resultArr = [];
					     
					     for(let i=0; i<json.length; i++){
					    	 const month_arr = [];
					    	 
					    	 month_arr.push(Number(json[i].m_01));
					    	 month_arr.push(Number(json[i].m_02));
					    	 month_arr.push(Number(json[i].m_03));
					    	 month_arr.push(Number(json[i].m_04));
					    	 month_arr.push(Number(json[i].m_05));
					    	 month_arr.push(Number(json[i].m_06));
					    	 month_arr.push(Number(json[i].m_07));
					    	 month_arr.push(Number(json[i].m_08));
					    	 month_arr.push(Number(json[i].m_09));
					    	 month_arr.push(Number(json[i].m_10));
					    	 month_arr.push(Number(json[i].m_11));
					    	 month_arr.push(Number(json[i].m_12));
					    	 
					    	 const obj = {name: json[i].categoryname,
					    		          data: month_arr};
					    	 
					    	 resultArr.push(obj); // 배열속에 객체를 넣기
					     }// end of for----------------------------------
					    
				         ///////////////////////////////////////////////////////////////////////
				    	 Highcharts.chart('chart_container', {
			
				    		    title: {
				    		        text: new Date().getFullYear()+'년 카테고리별 월별주문 통계'
				    		    },
			
				    		    subtitle: {
				    		        text: 'Source: <a href="https://irecusa.org/programs/solar-jobs-census/" target="_blank">출처</a>'
				    		    },
			
				    		    yAxis: {
				    		        title: {
				    		            text: '주문 금액'
				    		        }
				    		    },
			
				    		    xAxis: {
				    		        accessibility: {
				    		            rangeDescription: '범위: 1 to 12'
				    		        }
				    		    },
			
				    		    legend: {
				    		        layout: 'vertical',
				    		        align: 'right',
				    		        verticalAlign: 'middle'
				    		    },
			
				    		    plotOptions: {
				    		        series: {
				    		            label: {
				    		                connectorAllowed: false
				    		            },
				    		            pointStart: 1
				    		        }
				    		    },
			
				    		    series: resultArr,
			
				    		    responsive: {
				    		        rules: [{
				    		            condition: {
				    		                maxWidth: 500
				    		            },
				    		            chartOptions: {
				    		                legend: {
				    		                    layout: 'horizontal',
				    		                    align: 'center',
				    		                    verticalAlign: 'bottom'
				    		                }
				    		            }
				    		        }]
				    		    }
			
				    		});
					},
					error: function(request, status, error){
					   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					}
		          });
	         
			 break;
	}// end of switch (key) {}-----------------------
	  
  }// end of function func_choice(searchTypeVal)---------------

</script> 
    
<jsp:include page="../footer.jsp" />






    
