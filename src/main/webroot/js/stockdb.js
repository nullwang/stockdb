// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    url = url.toLowerCase(); // This is just to avoid case sensitiveness
    name = name.replace(/[\[\]]/g, "\\$&").toLowerCase();// This is just to avoid case sensitiveness for query parameter name
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

if (stockdb === undefined) {
	var stockdb = {};
    stockdb.baseFourMetrics=["day_opening_price","day_highest_price","day_lowest_price","day_closing_price"];
    stockdb.baseEightMetrics = [];
    for(ele in stockdb.baseFourMetrics){
        stockdb.baseEightMetrics.push(ele);
    }
    stockdb.baseEightMetrics.push("day_change");
    stockdb.baseEightMetrics.push("day_%chg");
    stockdb.baseEightMetrics.push("day_volume");
    stockdb.baseEightMetrics.push("day_turnover");
}

stockdb.ObjectMetric = function(id, name){
    this.id = id;
    this.metricName = name;
}

stockdb.MetricQuery = function(){
    this.startTime;
    this.endTime;
    this.objectMetrics = [];

    this.setStartTime = function(startTime){
        this.startTime = startTime;
        return this;
    }

    this.setEndTime = function(endTime){
        this.endTime = endTime;
        return this;
    }

    this.addMetric = function(id, metricName){
        this.objectMetrics.push(new stockdb.ObjectMetric(id,metricName));
        return this;
    }
}

stockdb.MetricException = function (message) {
	this.message = message;
};

stockdb.Aggregators =
{
	AVG: "avg",
	DEV: "dev",
	MAX: "max",
	MIN: "min",
	RATE: "rate",
	SAMPLER: "sampler",
	SORT: "sort",
	SUM: "sum",
	LEAST_SQUARES: "least_squares",
	PERCENTILE: "percentile",
	SCALE: "scale"
};

stockdb.Unit =  //Values used for Aggregator sampling and Relative time
{
	MILLISECONDS: "milliseconds",
	SECONDS: "seconds",
	MINUTES: "minutes",
	HOURS: "hours",
	DAYS: "days",
	WEEKS: "weeks",
	MONTHS: "months",
	YEARS: "years"
};



/**
 name: Name of the metric
 */
stockdb.Metric = function (name) {
	this.tags = {};
	this.name = name;
	this.aggregators;
	this.group_by;

	this.addGroupBy = function (groupBy) {
		if (!this.group_by) {
			this.group_by = [];
		}

		this.group_by.push(groupBy);
		return this;
	};

	this.addTag = function (name, value) {
		if (!this.tags[name]) {
			this.tags[name] = [];
		}

		this.tags[name].push(value);
		return this;
	};

	this.addRate = function (unit) {
		if (!this.aggregators)
			this.aggregators = [];

		var rate = {};
		rate.name = "rate";
		if (unit) {
			rate.unit = unit;
		}

		this.aggregators.push(rate);
		return this;
	};

	this.addSampler = function (unit) {
		if (!this.aggregators)
			this.aggregators = [];

		var rate = {};
		rate.name = "sampler";
		if (unit) {
			rate.unit = unit;
		}

		this.aggregators.push(rate);
		return this;
	};

	this.addPercentile = function (value, unit, percent) {
		if (!this.aggregators)
			this.aggregators = [];

		var percentile = {};
		percentile.name = "percentile";
		percentile.percentile = percent;
		if (unit) {
			percentile.sampling = {};
			percentile.sampling.unit = unit;
			percentile.sampling.value = value;
		}

		this.aggregators.push(percentile);
		return this;
	};

	this.addDivideAggregator = function (divisor) {
		if (!this.aggregators)
			this.aggregators = [];

		var aggregator = {};
		aggregator.name = "div";
		if (divisor) {
			aggregator.divisor= divisor;
		}

		this.aggregators.push(aggregator);
		return this;
	};

	this.addAggregator = function (name, value, unit) {
		if (!this.aggregators)
			this.aggregators = [];

		var aggregator = {};
		aggregator.name = name;
		aggregator.align_sampling = true;

		if (value && unit) {
			aggregator.sampling = {};
			aggregator.sampling.value = value;
			aggregator.sampling.unit = unit;
		}

		this.aggregators.push(aggregator);
		return this;
	}

	this.addScaleAggregator = function (scalingFactor) {
		if (!this.aggregators)
			this.aggregators = [];

		var aggregator = {};
		aggregator.name = 'scale';
		if (scalingFactor) {
            aggregator.factor = scalingFactor;
		}

        this.aggregators.push(aggregator);

	    return this;
	}
};

/**
 * Tag groupBy
 * @param tags  space or comma delimited list of tag names
 */
stockdb.TagGroupBy = function (tags) {
	this.name = "tag";
	this.tags = [];

	if (tags) {
		this.tags = tags.trim().split(/[\s,]+/);
	}
};

/**
 * Value groupBy
 * @param groupSize
 */
stockdb.ValueGroupBy = function (groupSize) {
	this.name = "value";
	this.range_size = groupSize;
};

/**
 * Time groupBy
 * @param groupSizeValue group size value
 * @param groupSizeUnit group size unit: milliseconds, seconds, minutes, hours, days, months, years
 * @param groupCount group count
 */
stockdb.TimeGroupBy = function (groupSizeValue, groupSizeUnit, groupCount) {
	this.name = "time";
	this.group_count = groupCount;
	this.range_size = {};

	this.range_size.value = groupSizeValue;
	this.range_size.unit = groupSizeUnit;
};



stockdb.showError = function (message) {
	alert(message);
};

//---------------------------------------------------------------------------
/**
 @param values Array of arrays of timestamp, value
 @returns: Array[0-6] each containing an array of values for that day
 */
stockdb.collectWeeklyValues = function (values) {
	var week = [];

	$.each(values, function (i, val) {
		var date = new Date(0);
		date.setUTCSeconds(val[0]);
		var day = date.getDay();
		if (week[day] == undefined)
			week[day] = [];

		week[day].push(val[1]);
	});

	return (week);
};


//---------------------------------------------------------------------------
/**
 Takes the return from collectWeeklyValues and averages them to one number
 @return: returns an array of numbers for each day of the week
 */
stockdb.averageWeeklyValues = function (weekData) {
	var weekAvg = [];

	for (i = 0; i < 7; i++) {
		if (weekData[i] == undefined) {
			weekAvg[i] = 0;
		}
		else {
			var dayValues = weekData[i];
			var sum = 0;
			dayValues.forEach(function (element) {
				sum += element;
			});
			weekAvg[i] = sum / dayValues.length;
		}
	}

	return weekAvg;
};

