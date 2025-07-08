<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../header1.jsp" />

<style>
    :root {
        --main-bg: #f8f9fa;
        --table-header-bg: #343a40;
        --table-header-color: #ffffff;
        --table-border-color: #dee2e6;
        --hover-color: #f1f3f5;
    }

    body {
        background-color: var(--main-bg);
        font-family: 'Segoe UI', sans-serif;
    }

    h2 {
        text-align: center;
        color: #343a40;
        font-weight: bold;
        margin-top: 50px;
    }

    #chart_container {
        margin: 40px auto;
        width: 95%;
        min-height: 400px;
    }

    #table_container {
        margin: 30px auto;
        width: 95%;
    }

    #table_container table {
        width: 100%;
        border-collapse: collapse;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
        background-color: #fff;
        font-size: 15px;
    }

    #table_container th, #table_container td {
        border: 1px solid var(--table-border-color);
        padding: 10px;
        text-align: center;
    }

    #table_container th {
        background-color: var(--table-header-bg);
        color: var(--table-header-color);
    }

    #table_container tr:hover {
        background-color: var(--hover-color);
    }
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- Highcharts Scripts -->
<script src="<%= ctxPath %>/Highcharts-10.3.1/code/highcharts.js"></script>
<script src="<%= ctxPath %>/Highcharts-10.3.1/code/modules/exporting.js"></script>
<script src="<%= ctxPath %>/Highcharts-10.3.1/code/modules/export-data.js"></script>
<script src="<%= ctxPath %>/Highcharts-10.3.1/code/modules/accessibility.js"></script>
<script src="<%= ctxPath %>/Highcharts-10.3.1/code/modules/series-label.js"></script>

<div style="width: 90%; margin: auto; min-height: 1000px;">
    <br><br><br>
    <h2>${sessionScope.loginuser.name} 주문통계 차트</h2>

    <form name="searchFrm" style="margin: 30px 0;">
        <select name="searchType" id="searchType">
            <option value="categoryByMonth">카테고리별 월별 매출 통계</option>
            <option value="dailySales">일별 매출 통계</option>
            <!-- 추가 옵션은 여기서 더 넣을 수 있습니다 -->
        </select>
    </form>

    <div id="chart_container"></div>
    <div id="table_container"></div>
</div>

<script>
$(function() {
    loadChartByType($("#searchType").val());

    $("#searchType").change(function() {
        loadChartByType($(this).val());
    });
});

function loadChartByType(type) {
    if(type === "dailySales") {
        loadDailySales();
    } else {
        loadAdminCategorySalesByMonth();
    }
}

function loadAdminCategorySalesByMonth() {
    $.ajax({
        url: "<%= ctxPath %>/myshop/adminCategorySalesByMonthJSON.go",
        dataType: "json",
        success: function(json) {
            $("#chart_container").empty();
            $("#table_container").empty();

            var seriesData = [];

            for (var i = 0; i < json.length; i++) {
                var month_arr = [];
                for (var m = 1; m <= 12; m++) {
                    var key = "m_" + ("0" + m).slice(-2);
                    var val = json[i][key];
                    month_arr.push(val == null ? 0 : Number(val));
                }
                seriesData.push({
                    name: json[i].categoryname,
                    data: month_arr
                });
            }

            Highcharts.chart('chart_container', {
                title: {
                    text: new Date().getFullYear() + '년 카테고리별 월별 매출 통계'
                },
                yAxis: {
                    title: {
                        text: '매출 금액 (원)'
                    }
                },
                xAxis: {
                    categories: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                    title: {
                        text: '월'
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
                        }
                    }
                },
                series: seriesData,
                responsive: {
                    rules: [{
                        condition: {
                            maxWidth: 600
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

            // 테이블 만들기
            var html = "<table>";
            html += "<tr><th>카테고리</th>";
            for (var i = 1; i <= 12; i++) {
                html += "<th>" + (i < 10 ? "0" + i : i) + "월</th>";
            }
            html += "</tr>";

            for (var i = 0; i < json.length; i++) {
                html += "<tr><td>" + json[i].categoryname + "</td>";
                for (var m = 1; m <= 12; m++) {
                    var key = "m_" + ("0" + m).slice(-2);
                    var val = json[i][key];
                    val = val == null ? 0 : Number(val);
                    html += "<td>" + val.toLocaleString() + "원</td>";
                }
                html += "</tr>";
            }
            html += "</table>";

            $("#table_container").html(html);
        },
        error: function(request, status, error) {
            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
        }
    });
}

function loadDailySales() {
    $.ajax({
        url: "<%= ctxPath %>/myshop/dailySalesRecentMonthJSON.go",
        dataType: "json",
        success: function(json) {
            $("#chart_container").empty();
            $("#table_container").empty();

            var categories = [];
            var data = [];

            for (var i = 0; i < json.length; i++) {
                categories.push(json[i].date);  
                data.push(json[i].date_total == null ? 0 : Number(json[i].date_total)); 
            }

            Highcharts.chart('chart_container', {
                chart: {
                    type: 'line'
                },
                title: {
                    text: '일별 매출 통계'
                },
                xAxis: {
                    categories: categories,
                    title: {
                        text: '날짜'
                    }
                },
                yAxis: {
                    title: {
                        text: '매출 금액 (원)'
                    }
                },
                series: [{
                    name: '매출',
                    data: data
                }],
                responsive: {
                    rules: [{
                        condition: {
                            maxWidth: 600
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

            // 테이블 생성
            var html = "<table>";
            html += "<tr><th>날짜</th><th>매출 금액</th></tr>";

            for (var i = 0; i < json.length; i++) {
                var total = json[i].date_total == null ? 0 : Number(json[i].date_total);
                html += "<tr>";
                html += "<td>" + json[i].date + "</td>";
                html += "<td>" + total.toLocaleString() + "원</td>";
                html += "</tr>";
            }

            html += "</table>";
            $("#table_container").html(html);
        },
        error: function(request, status, error) {
            alert("code: " + request.status + "\nmessage: " + request.responseText + "\nerror: " + error);
        }
    });
}
</script>
