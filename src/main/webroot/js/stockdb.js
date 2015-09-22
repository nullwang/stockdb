if (stockdb === undefined) {
	var stockdb = {};
    stockdb.baseFourMetrics=["day_opening_price","day_highest_price","day_lowest_price","day_closing_price"];
    stockdb.baseEightMetrics = [];
    for(ele in baseFourMetrics){
        stockdb.baseEightMetrics.push(ele);
    }
    baseEightMetrics.push("day_change");
    baseEightMetrics.push("day_%chg");
    baseEightMetrics.push("day_volume");
    baseEightMetrics.push("day_turnover");
}

stockdb.ObjectMetric = function(id, name){
    this.id = id;
    this.name = name;
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
        this.objectMetrics.push(new ObjectMetric(id,metricName));
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

