<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../header1.jsp" />

<style>
    :root {
        --main-bg: #f4f7fa;
        --table-header-bg: #2c3e50;
        --table-header-color: #ffffff;
        --table-border-color: #dcdcdc;
        --hover-color: #eaf4ff;
    }

    body {
        background-color: var(--main-bg);
        font-family: 'Segoe UI', sans-serif;
    }

    select#searchType {
        height: 45px;
        width: 300px;
        padding: 0 10px;
        font-size: 16px;
    }

    h2 {
        text-align: center;
        color: #333;
        font-weight: bold;
    }

    div#chart_container {
        margin-bottom: 40px;
    }

    div#table_container table {
        width: 100%;
        border-collapse: collapse;
        box-shadow: 0 0 10px rgba(0,0,0,0.05);
        background-color: #fff;
        font-size: 15px;
    }

    div#table_container th, div#table_container td {
        border: 1px solid var(--table-border-color);
        padding: 10px 12px;
        text-align: center;
    }

    div#table_container th {
        background-color: var(--table-header-bg);
        color: var(--table-header-color);
    }

    div#table_container tr:hover {
        background-color: var(--hover-color);
    }
</style>

<!-- Highcharts Scripts -->
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/highcharts.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/exporting.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/export-data.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/accessibility.js"></script>
<script src="<%= ctxPath%>/Highcharts-10.3.1/code/modules/series-label.js"></script>

<div style="width: 90%; margin: auto; min-height: 1000px;">
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
    <h2>${sessionScope.loginuser.name} 님의 주문통계 차트</h2>
	
    <form name="searchFrm" style="margin: 30px 0;">
        <select name="searchType" id="searchType">
            <option value="">통계선택하세요</option>
            <option value="myPurchase_byCategory">나의 카테고리별주문 통계</option>
            <option value="myPurchase_byMonth_byCategory">나의 카테고리별 월별주문 통계</option>
        </select>
    </form>

    <div id="chart_container"></div>
    <div id="table_container"></div>
</div>

<script>
$(function() {
    $('#searchType').change(function(e) {
        func_choice($(this).val());
    });

    $('#searchType').val("myPurchase_byCategory").trigger("change");
});

function func_choice(searchTypeVal) {
    if (searchTypeVal === "") {
        $("#chart_container").empty();
        $("#table_container").empty();
        return;
    }

    if (searchTypeVal === "myPurchase_byCategory") {
        $.ajax({
            url: "<%= ctxPath%>/shop/myPurchase_byCategoryJSON.go",
            data: { userid: "${sessionScope.loginuser.userid}" },
            dataType: "json",
            success: function(json) {
                $("#chart_container").empty();
                $("#table_container").empty();

                let resultArr = [];

                for (let i = 0; i < json.length; i++) {
                    let obj = {
                        name: json[i].categoryname,
                        y: Number(json[i].sumpay_pct)
                    };
                    if (i === 0) {
                        obj.sliced = true;
                        obj.selected = true;
                    }
                    resultArr.push(obj);
                }

                Highcharts.chart('chart_container', {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: '나의 카테고리별 주문금액 비율'
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

                // 테이블 
                let html = "<table>";
                html += "<tr><th>카테고리</th><th>주문수</th><th>총주문금액</th><th>비율</th></tr>";

                $.each(json, function(index, item) {
                    html += "<tr>" +
                        "<td>" + item.categoryname + "</td>" +
                        "<td>" + item.cnt + "</td>" +
                        "<td style='text-align:right'>" + parseInt(item.sumpay).toLocaleString() + " 원</td>" +
                        "<td>" + parseFloat(item.sumpay_pct).toFixed(2) + " %</td>" +
                        "</tr>";
                });

                html += "</table>";
                $("#table_container").html(html);
            },
            error: function(request, status, error) {
                alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
            }
        });
    }

    else if (searchTypeVal === "myPurchase_byMonth_byCategory") {
       
    	 $.ajax({
				url:"<%= ctxPath%>/shop/myPurchase_byMonth_byCategoryJSON.go",
				data:{"userid":"${sessionScope.loginuser.userid}"},
				dataType:"json",
				success:function(json){
				    
				 // console.log(JSON.stringify(json));
				
				     $("div#chart_container").empty();
				     $("div#table_container").empty();
				  
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
				    ////////////////////////////////////////////////////////////
				    
			    	 let html =  "<table>";
	                     html += "<tr>" +
	                               "<th>카테고리</th>" +
	                               "<th>01월</th>" +
	                               "<th>02월</th>" +
	                               "<th>03월</th>" +
	                               "<th>04월</th>" +
	                               "<th>05월</th>" +
	                               "<th>06월</th>" +
	                               "<th>07월</th>" +
	                               "<th>08월</th>" +
	                               "<th>09월</th>" +
	                               "<th>10월</th>" +
	                               "<th>11월</th>" +
	                               "<th>12월</th>" +
	                             "</tr>";
	                            
	                 $.each(json, function(index, item){
	                    	html += "<tr>" +
	                    	            "<td>"+ item.categoryname +"</td>" +
	                    	            "<td>"+ Number(item.m_01).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_02).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_03).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_04).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_05).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_06).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_07).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_08).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_09).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_10).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_11).toLocaleString('en') +"원</td>" +
	                    	            "<td>"+ Number(item.m_12).toLocaleString('en') +"원</td>" +
	                    	        "</tr>";
	                 });        
	                            
                  html += "</table>";
                 
                  $("div#table_container").html(html); 
				},
				
				error: function(request, status, error){
				   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
	        });
    }
}
</script>
