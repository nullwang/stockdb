if (!stockdb) {
    var stockdb = {};
}

//用于查询 stock db 获取相应数据
stockdb.statusCallback = function(){
    starting = function(){
        var exeStartTime = new Date();
        $status.html("<i>正在查询...</i>");
        return exeStartTime;
    }

    //回调时会传入话费时间，毫秒单位
    doing = function(startTime){
        $status.html("<i>正在处理...</i>");
        $queryTime.html(numeral(new Date().getTime() - startTime.getTime()).format('0,0') + " ms");
    };

    completed = function(startTime){
        $status.html("<i>处理完成</i>");
        $queryTime.html(numeral(new Date().getTime() - startTime.getTime()).format('0,0') + " ms");
    };

    error = function(errMsg, startTime){
        $status.html(errMsg);
        $queryTime.html(numeral(new Date().getTime() - startTime.getTime()).format('0,0') + " ms");
    };
}


//用来查询k 线图
stockdb.kline = function(id, dataCallback)
{
    var startTime= stockdb.statusCallback.starting();
    $.ajax({
        type: "GET",
        url: stockdb.rootUrl+"stock/kline/"+id,
        headers: { 'Content-Type': ['application/json']},
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            stockdb.statusCallback.doing(startTime);
            setTimeout(function(){
                dataCallback(data);
            }, 0);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var $errorContainer = new String();
            $errorContainer.append("Status Code: " +  jqXHR.status + "</br>");
            $errorContainer.append("Status: " +  jqXHR.statusText + "<br>");
            $errorContainer.append("Return Value: " +  jqXHR.responseText);
            stockdb.statusCallback.error($errorContainer,startTime);
        }
    });
}

/**查询指定stock的名称,id 为股票代码**/
stockdb.queryObjName = function(id,callback){
    return stockdb.queryObjectAttr(id,"name",callback);
}

/**对象属性查询*/
stockdb.queryObjectAttr= function(id,attrName){
    var _url;
    if(! attrName) {
        _url = stockdb.rootUrl+"api/object/" + id ;
    }else{
        _url = stockdb.rootUrl+"api/object/" + id + "/" + attrName;
    }

    return JSON.parse( $.ajax({
        type: "GET",
        url: _url,
        headers: { 'Content-Type': ['application/json']},
        dataType: 'json',
        async:false
    }).responseText );
}

//根据[startTime,endTime] 查询指定 id， 指定 metricName的数据点集
stockdb.listDataPoints = function (id, metricName, startTime, endTime,
                                   dataCallback,statusCallback) {
    var exeStartTime = new Date();
    if( statusCallback) statusCallback.starting();

    $.ajax({
        type: "GET",
        url: "api/"+id + "/" + metricName + "/" + startTime + "/" + endTime,
        headers: { 'Content-Type': ['application/json']},
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            if(statusCallback) statusCallback.doing(new Date().getTime() - exeStartTime.getTime());
            setTimeout(function(){
                dataCallback(data);
            }, 0);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var $errorContainer = $("#errorContainer");
            $errorContainer.show();
            $errorContainer.html("");
            $errorContainer.append("Status Code: " +  jqXHR.status + "</br>");
            $errorContainer.append("Status: " +  jqXHR.statusText + "<br>");
            $errorContainer.append("Return Value: " +  jqXHR.responseText);
            if(statusCallback) statusCallback.error(new Date().getTime() - exeStartTime.getTime());
        }
    });
};

stockdb.queryMetricsDataPoints = function(id, metricNames, startTime, endTime, callback){
    var metricQuery = new stockdb.MetricQuery();
    metricQuery.setStartTime(startTime);
    metricQuery.setEndTime(endTime);
    for(index in metricNames){
        metricQuery.addMetric(id, metricNames[index]);
    }

    stockdb.queryDataPoints(metricQuery,callback);
}





/**自定义数据查询**/
stockdb.queryDataPoints = function (metricQuery, callback) {
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
				callback(data);
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
				callback(data);
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
