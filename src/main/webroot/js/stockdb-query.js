if (stockdb === undefined)
{
	var stockdb = {};
}

stockdb.listDataPoints = function (dataQuery, callback) {
    var startTime = new Date();

    var $status = $('#status');
    var $queryTime = $("#queryTime");

    $status.html("<i>正在查询...</i>");

    $.ajax({
        type: "POST",
        url: "api/v1/data/query",
        headers: { 'Content-Type': ['application/json']},
        data: JSON.stringify(dataQuery),
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            $status.html("<i>正在处理...</i>");
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


stockdb.dataPointsQuery = function (metricQuery, callback) {
	var startTime = new Date();

	var $status = $('#status');
	var $queryTime = $("#queryTime");

	$status.html("<i>正在查询...</i>");

	$.ajax({
		type: "POST",
		url: "api/v1/datapoints/query",
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
