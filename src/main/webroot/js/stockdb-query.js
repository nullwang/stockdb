if (stockdb === undefined)
{
	var stockdb = {};
}

//根据[startTime,endTime) 查询指定 id， 指定 metricName的数据点集
stockdb.listDataPoints = function (id, metricName, startTime, endTime, callback) {
    var exeStartTime = new Date();

    var $status = $('#status');
    var $queryTime = $("#queryTime");

    $status.html("<i>正在查询...</i>");

    $.ajax({
        type: "GET",
        url: "api/"+id + "/" + metricName + "/" + startTime + "/" + endTime,
        headers: { 'Content-Type': ['application/json']},
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            $status.html("<i>正在处理...</i>");
            $queryTime.html(numeral(new Date().getTime() - exeStartTime.getTime()).format('0,0') + " ms");
            setTimeout(function(){
                callback(data.queries);
            }, 0);
        },
        error: function (jqXHR, textStatus, errorThrown) {

            var $errorContainer = $("#errorContainer");
            $errorContainer.show();
            $errorContainer.html("");
            $errorContainer.append("Status Code: " +  jqXHR.status + "</br>");
            $errorContainer.append("Status: " +  jqXHR.statusText + "<br>");
            $errorContainer.append("Return Value: " +  jqXHR.responseText);

            $status.html("");
            $queryTime.html(numeral(new Date().getTime() - exeStartTime.getTime()).format('0,0') + " ms");
        }
    });
};

/**查询指定stock的基础四项指标**/
stockdb.queryBaseFourMetricDataPoints = function(id, startTime, endTime, callback){
    stockdb.queryMetricsDataPoints(id,stockdb.baseFourMetrics);
}

stockdb.queryMetricsDataPoints = function(id, metricNames, startTime, endTime, callback){
    var metricQuery = new stockdb.MetricQuery();
    for(metricName in metricNames){
        metricQuery.addMetric(id, metricName);
    }

    stockdb.dataPointsQuery(metricQuery,callback);
}


/**自定义查询**/
stockdb.dataPointsQuery = function (metricQuery, callback) {
	var startTime = new Date();

	var $status = $('#status');
	var $queryTime = $("#queryTime");

	$status.html("<i>正在查询...</i>");

	$.ajax({
		type: "POST",
		url: "api/query",
		headers: { 'Content-Type': ['application/json']},
		data: JSON.stringify(metricQuery),
		dataType: 'json',
		success: function (data, textStatus, jqXHR) {
			$status.html("<i>Plotting in progress...</i>");
			$queryTime.html(numeral(new Date().getTime() - startTime.getTime()).format('0,0') + " ms");
			setTimeout(function(){
				callback(data.queries);
			}, 0);
		},
		error: function (jqXHR, textStatus, errorThrown) {

			var $errorContainer = $("#errorContainer");
			$errorContainer.show();
			$errorContainer.html("");
			$errorContainer.append("Status Code: " +  jqXHR.status + "</br>");
			$errorContainer.append("Status: " +  jqXHR.statusText + "<br>");
			$errorContainer.append("Return Value: " +  jqXHR.responseText);

			$status.html("");
			$queryTime.html(numeral(new Date().getTime() - startTime.getTime()).format('0,0') + " ms");
		}
	});
};

stockdb.deleteDataPoints = function (deleteQuery, callback) {
	var startTime = new Date();

	var $status = $('#status');
	var $queryTime = $("#queryTime");

	$status.html("<i>Delete in progress...</i>");

	$.ajax({
		type: "POST",
		url: "api/v1/datapoints/delete",
		headers: { 'Content-Type': ['application/json']},
		data: deleteQuery,
		dataType: 'text',
		success: function (data, textStatus, jqXHR) {
			setTimeout(function () {
				callback(data.queries);
			}, 0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			var $errorContainer = $("#errorContainer");
			$errorContainer.show();
			$errorContainer.html("");
			$errorContainer.append("Status Code: " + jqXHR.status + "</br>");
			$errorContainer.append("Status: " + jqXHR.statusText + "<br>");
			$errorContainer.append("Return Value: " + jqXHR.responseText);

			$status.html("");
		}
	});
};
