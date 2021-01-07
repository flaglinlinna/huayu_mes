var demo_tasks = {
	"data":[
		{"id":1, "text":"配合基建影响线路停电","start_date":"01-10-2018","end_date":"","duration":"8","stopUpProDesc":"",
			"matterNeedAtt":"","modeAdj":"","riskGrade":"","specialEqu":"", "open": true},

        {"id":2, "text":"新设备投产", "start_date":"01-10-2018","end_date":"","duration":"9","stopUpProDesc":"",
			"matterNeedAtt":"","modeAdj":"","riskGrade":"","specialEqu":"","open": true},

        {"id":3, "text":"250kv及以上计划线路检修、需220kv及以上线路陪停的变电检修", "start_date":"01-10-2018","end_date":"","duration":"","stopUpProDesc":"",
			"matterNeedAtt":"","modeAdj":"","riskGrade":"","specialEqu":"","open": true},

		{"id":20, "text":"500KV联变计划检修","start_date":"01-10-2018","end_date":"","duration":"9", "stopUpProDesc":"",
			"matterNeedAtt":"","modeAdj":"","riskGrade":"","specialEqu":"","parent":"2", "open": true},

        {"id":12, "text":"220kV田九Ⅰ路", "start_date":"03-10-2018","end_date":"","duration":"5","stopUpProDesc":"基建：220kV田九Ⅰ路停电配合九龙-同心220kV线路工程#1、#2塔组立铁塔施工安全距离不足做安全措施。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"1", "open": true},

        {"id":13, "text":"220kV新童Ⅱ路", "start_date":"02-10-2018","end_date":"", "duration":"7", "stopUpProDesc":"基建：220kV新童Ⅱ路#2塔脱开新岭变接入安平",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"1",  "open": true},

        {"id":14, "text":"220kV闽高Ⅰ路", "start_date":"02-10-2018","end_date":"", "duration":"6", "stopUpProDesc":"基建：220kV高岐~闽清开断进超山变",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"1", "open": true},

        {"id":15, "text":"220kV宝平Ⅱ路", "start_date":"02-10-2018","end_date":"", "duration":"5","stopUpProDesc":"基建：220kV宝平Ⅱ路停电改造#44-。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"1",  "open": true},

		{"id":32, "text":"#2联变", "start_date":"20-10-2018","end_date":"", "duration":"8", "stopUpProDesc":"A类检修：漳州变#2联变5011开关CT更换、耐压试验。",
			"matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"20", "open": true},

		{"id":33, "text":"500kVⅡ段母线", "start_date":"11-10-2018","end_date":"", "duration":"8", "stopUpProDesc":"500KV Ⅱ_Ⅳ段母分5002开关，500KV GIS II母一、二次设备预防性维修及改造。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"20", "open": true},

		{"id":4, "text":"#2主变", "start_date":"13-10-2018","end_date":"", "duration":"6","stopUpProDesc":"#2主变/#2主变5023开关/#2主变_玉顶Ⅱ路5022开关预防性维修，三相联动分合闸指示装置变更。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"20", "open": true},

		{"id":5, "text":"#3联变", "start_date":"02-10-2018","end_date":"", "duration":"7","stopUpProDesc":"C类检修：#3联变及开关保护、综自首检。#3联变故障录波首检，#3联变消防维护及消缺。#3联变绝缘子串检查及测零。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"20",  "open": true},

		{"id":6, "text":"220kV和龙Ⅰ路", "start_date":"03-10-2018","end_date":"", "duration":"7","stopUpProDesc":"220kV和龙Ⅰ路#9-#36更换绝缘子",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"3", "open": true},

		{"id":7, "text":"500kV后漳Ⅰ路", "start_date":"11-10-2018","end_date":"", "duration":"8","stopUpProDesc":"年检",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"3", "open": true},

		{"id":8, "text":"500kV岭大Ⅱ路", "start_date":"14-10-2018","end_date":"", "duration":"5","stopUpProDesc":"5054开关预试、线路及短线（地上、地下）保护校验、出现场设备、GIS设备、2号电缆线清扫检查及试验、3号主变压器小修、4号主变压器小修",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"3",  "open": true},

		{"id":9, "text":"1000kV都榕II线", "start_date":"14-10-2018","end_date":"", "duration":"4","stopUpProDesc":"C类检修：榕城变1000kV都榕Ⅱ线线路及高抗、T021、T022开关间隔一次设备例检及二次传动试验。高抗消防系统检查及试验。T021开关漏气处理。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"3", "open": true},

		{"id":10, "text":"500kV水莆线", "start_date":"14-10-2018","end_date":"", "duration":"3", "stopUpProDesc":"线路：水莆线#1-#52右地线及金具大修。水莆线#140、#141、#227、#227+1等地线绝缘子单挂点改双挂点大修。C类检修：水莆线线路消缺。",
            "matterNeedAtt":"无","modeAdj":"M","riskGrade":"无","specialEqu":"无","parent":"3", "open": true}
	]
};

var users_data = {
	"data":[
		{"id":1, "text":"Project #1", "start_date":"01-04-2020", "duration":"11", "progress": 0.6, "open": true, "users": "John", "priority": "2","descrip":""},
		{"id":2, "text":"Task #1", "start_date":"03-04-2020", "duration":"5", "parent":"1", "progress": 1, "open": true, "users": "Mike", "priority": "1","descrip":"按照进度完成"},
		{"id":3, "text":"Task #2", "start_date":"02-04-2020", "duration":"7", "parent":"1", "progress": 0.5, "open": true, "users": "Anna", "priority": "1","descrip":""},
		{"id":4, "text":"Task #3", "start_date":"02-04-2020", "duration":"6", "parent":"1", "progress": 0.8, "open": true, "users": "Mike", "priority": "2","descrip":""},
		{"id":5, "text":"Task #4", "start_date":"02-04-2020", "duration":"5", "parent":"1", "progress": 0.2, "open": true, "users": "John", "priority": "3","descrip":""},
		{"id":6, "text":"Task #5", "start_date":"02-04-2020", "duration":"7", "parent":"1", "progress": 0, "open": true, "users": "John", "priority": "2","descrip":""},
		{"id":7, "text":"Task #2.1", "start_date":"03-04-2020", "duration":"2", "parent":"3", "progress": 1, "open": true, "users": "Mike", "priority": "2","descrip":"良好"},
		{"id":8, "text":"Task #2.2", "start_date":"06-04-2020", "duration":"3", "parent":"3", "progress": 0.8, "open": true, "users": "Anna", "priority": "3","descrip":""},
		{"id":9, "text":"Task #2.3", "start_date":"10-04-2020", "duration":"4", "parent":"3", "progress": 0.2, "open": true, "users": "Mike", "priority": "1","descrip":""},
		{"id":10, "text":"Task #2.4", "start_date":"10-04-2020", "duration":"4", "parent":"3", "progress": 0, "open": true, "users": "John", "priority": "1","descrip":""},
		{"id":11, "text":"Task #4.1", "start_date":"03-04-2020", "duration":"4", "parent":"5", "progress": 0.5, "open": true, "users": "John", "priority": "3","descrip":""},
		{"id":12, "text":"Task #4.2", "start_date":"03-04-2020", "duration":"4", "parent":"5", "progress": 0.1, "open": true, "users": "John", "priority": "3","descrip":""},
		{"id":13, "text":"Task #4.3", "start_date":"03-04-2020", "duration":"5", "parent":"5", "progress": 0, "open": true, "users": "Anna", "priority": "3","descrip":""}
	],
	/*"links":[
		{"id":"10","source":"11","target":"12","type":"1"},
		{"id":"11","source":"11","target":"13","type":"1"}
	]*/
};

var users_data_john = {
		"data":[
			{"id":1, "text":"Project #1", "start_date":"01-04-2020", "duration":"11", "progress": 0.6, "open": true, "users": "John", "priority": "2"},
			{"id":5, "text":"Task #4", "start_date":"02-04-2020", "duration":"5", "parent":"1", "progress": 0.2, "open": true, "users": "John", "priority": "3"},
			{"id":6, "text":"Task #5", "start_date":"02-04-2020", "duration":"7", "parent":"1", "progress": 0, "open": true, "users": "John", "priority": "2"},
			{"id":10, "text":"Task #2.4", "start_date":"10-04-2020", "duration":"4", "parent":"3", "progress": 0, "open": true, "users": "John", "priority": "1"},
			{"id":11, "text":"Task #4.1", "start_date":"03-04-2020", "duration":"4", "parent":"5", "progress": 0.5, "open": true, "users": "John", "priority": "3"},
			{"id":12, "text":"Task #4.2", "start_date":"03-04-2020", "duration":"4", "parent":"5", "progress": 0.1, "open": true, "users": "John", "priority": "3"},
		],
		/*"links":[
			{"id":"10","source":"11","target":"12","type":"1"},
			{"id":"11","source":"11","target":"13","type":"1"}
		]*/
	};

var projects_with_milestones = {
	"data":[
		{"id":11, "text":"Project #1", type:gantt.config.types.project, "progress": 0.6, "open": true},

		{"id":12, "text":"Task #1", "start_date":"03-04-2018", "duration":"5", "parent":"11", "progress": 1, "open": true},
		{"id":13, "text":"Task #2", "start_date":"03-04-2018", type:gantt.config.types.project, "parent":"11", "progress": 0.5, "open": true},
		{"id":14, "text":"Task #3", "start_date":"02-04-2018", "duration":"6", "parent":"11", "progress": 0.8, "open": true},
		{"id":15, "text":"Task #4", type:gantt.config.types.project, "parent":"11", "progress": 0.2, "open": true},
		{"id":16, "text":"Final milestone", "start_date":"15-04-2018", type:gantt.config.types.milestone, "parent":"11", "progress": 0, "open": true},

		{"id":17, "text":"Task #2.1", "start_date":"03-04-2018", "duration":"2", "parent":"13", "progress": 1, "open": true},
		{"id":18, "text":"Task #2.2", "start_date":"06-04-2018", "duration":"3", "parent":"13", "progress": 0.8, "open": true},
		{"id":19, "text":"Task #2.3", "start_date":"10-04-2018", "duration":"4", "parent":"13", "progress": 0.2, "open": true},
		{"id":20, "text":"Task #2.4", "start_date":"10-04-2018", "duration":"4", "parent":"13", "progress": 0, "open": true},
		{"id":21, "text":"Task #4.1", "start_date":"03-04-2018", "duration":"4", "parent":"15", "progress": 0.5, "open": true},
		{"id":22, "text":"Task #4.2", "start_date":"03-04-2018", "duration":"4", "parent":"15", "progress": 0.1, "open": true},
		{"id":23, "text":"Mediate milestone", "start_date":"14-04-2018", type:gantt.config.types.milestone, "parent":"15", "progress": 0, "open": true}
	],
	"links":[
		{"id":"10","source":"11","target":"12","type":"1"},
		{"id":"11","source":"11","target":"13","type":"1"},
		{"id":"12","source":"11","target":"14","type":"1"},
		{"id":"13","source":"11","target":"15","type":"1"},
		{"id":"14","source":"23","target":"16","type":"0"},
		{"id":"15","source":"13","target":"17","type":"1"},
		{"id":"16","source":"17","target":"18","type":"0"},
		{"id":"17","source":"18","target":"19","type":"0"},
		{"id":"18","source":"19","target":"20","type":"0"},
		{"id":"19","source":"15","target":"21","type":"2"},
		{"id":"20","source":"15","target":"22","type":"2"},
		{"id":"21","source":"15","target":"23","type":"0"}
	]
};

var projects_milestones_critical = {
	data: [
		{ id: 1, text: "Office itinerancy", type: "project", progress: 0.4, open: true, start_date: "02-04-2018 00:00", duration: 17, parent: 0 },
		{ id: 2, text: "Office facing", type: "project", start_date: "02-04-2018 00:00", duration: 5, progress: 0.6, parent: 1, open: true},
		{ id: 5, text: "Interior office", type: "task", start_date: "02-04-2018 00:00", duration: 3, parent: 2, progress: 0.6, open: true},
		{ id: 6, text: "Air conditioners check", type: "task",  start_date: "05-04-2018 00:00", duration: 2, parent: 2, progress: 0.29, open: true},
		{ id: 3, text: "Furniture installation", type: "project", start_date: "08-04-2018 00:00", duration: 2, parent: 1, progress: 0.6, open: false},
		{ id: 7, text: "Workplaces preparation", type: "task",  start_date: "08-04-2018 00:00", duration: 2, parent: 3, progress: 0.6, open: true},
		{ id: 4, text: "The employee relocation", type: "project", start_date: "10-04-2018 00:00", duration: 9, parent: 1, progress: 0.5, open: true},
		{ id: 8, text: "Preparing workplaces", type: "task",  start_date: "10-04-2018 00:00", duration: 3, parent: 4, progress: 0.5, open: true},
		{ id: 9, text: "Workplaces importation", type: "task",  start_date: "13-04-2018 00:00", duration: 3, parent: 4, progress: 0.5, open: true},
		{ id: 10, text: "Workplaces exportation", type: "task",  start_date: "16-04-2018 00:00", duration: 3, parent: 4, progress: 0.5, open: true},
		{ id: 11, text: "Product launch", type: "project", progress: 0.6, open: true, start_date: "02-04-2018 00:00", duration: 17, parent: 0 },
		{ id: 12, text: "Perform Initial testing", type: "task",  start_date: "02-04-2018 00:00", duration: 5, parent: 11, progress: 1, open: true},
		{ id: 13, text: "Development", type: "project", start_date: "03-04-2018 00:00", duration: 16, parent: 11, progress: 0.5, open: true},
		{ id: 17, text: "Develop System", type: "task",  start_date: "03-04-2018 00:00", duration: 5, parent: 13, progress: 1, open: true},
		{ id: 25, text: "Beta Release", type: "milestone", start_date: "08-04-2018 00:00", duration: 0, parent: 13, progress: 0, open: true},
		{ id: 18, text: "Integrate System", type: "task",  start_date: "08-04-2018 00:00", duration: 4, parent: 13, progress: 0.8, open: true},
		{ id: 19, text: "Test", type: "task",  start_date: "12-04-2018 00:00", duration: 3, parent: 13, progress: 0.2, open: true},
		{ id: 20, text: "Marketing", type: "task",  start_date: "15-04-2018 00:00", duration: 4, parent: 13, progress: 0, open: true},
		{ id: 14, text: "Analysis", type: "task",  start_date: "02-04-2018 00:00", duration: 4, parent: 11, progress: 0.8, open: true},
		{ id: 15, text: "Design", type: "project", start_date: "06-04-2018 00:00", duration: 6, parent: 11, progress: 0.2, open: true},
		{ id: 21, text: "Design database", type: "task",  start_date: "06-04-2018 00:00", duration: 4, parent: 15, progress: 0.5, open: true},
		{ id: 22, text: "Software design", type: "task",  start_date: "08-04-2018 00:00", duration: 4, parent: 15, progress: 0.1, open: true},
		{ id: 16, text: "Documentation creation", type: "task",  start_date: "11-04-2018 00:00", duration: 5, parent: 11, progress: 0, open: true},
		{ id: 24, text: "Release v1.0", type: "milestone",  start_date: "19-04-2018 00:00", duration: 0, parent: 11, progress: 0, open: true}
	],
	links: [
		{ id: 1, source: "2", target: "3", type: "0"},
		{ id: 2, source: "3", target: "4", type: "0"},
		{ id: 3, source: "17", target: "25", type: "0"},
		{ id: 4, source: "18", target: "19", type: "0"},
		{ id: 5, source: "19", target: "20", type: "0"},
		{ id: 6, source: "13", target: "24", type: "0"},
		{ id: 7, source: "25", target: "18", type: "0"},
		{ id: 8, source: "5", target: "6", type: "0"},
		{ id: 9, source: "8", target: "9", type: "0"},
		{ id: 10, source: "9", target: "10", type: "0"},
		{ id: 11, source: "16", target: "24", type: "0"},
		{ id: 12, source: "14", target: "15", type: "0"}
	]
};

function generateData(count, dateFrom, dateTo){
	var tasks =  {
		data:[],
		links:[]
	};

	count = parseInt(count, 10) || 100;

	var date = new Date(dateFrom.getFullYear(),5,1);
	var project_id = 1;
	tasks.data.push({
		id:  project_id,
		text: "Project1",
		start_date: date,
		type: gantt.config.types.project,
		open:true
	});
	for (var i = 1; i < count; i++) {
		date = gantt.date.add(date, 1, "day");
		var task = {
			id: i + 1,
			start_date: date,
			text: "Task " + (i + 1),
			duration: 8,
			parent: project_id
		};

		if(gantt.date.add(date, 8, "day").valueOf() > dateTo.valueOf()){
			date = new Date(dateFrom);
			project_id = i + 1;
			delete task.parent;
			task.open = true;
		}
		tasks.data.push(task);

	}
	return tasks;
}