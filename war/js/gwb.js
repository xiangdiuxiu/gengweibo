var WEIBO_MAP = {};

var CONTEXT = null;
function getContext() {
	if (!CONTEXT) {
		var href = location.href;
		href = href.substring(7);
		var index = href.indexOf('/');
		if (index > 0 && index < href.length - 1) {
			var endIndex = href.indexOf('/', index + 1);
			CONTEXT = href.substring(index, endIndex);
		} else {
			CONTEXT = "";
		}
//		alert(CONTEXT);
	}
	return CONTEXT;
}

function render(html, argMap) {
	for (var i in argMap) {
		var key = '${' + i + '}';
		var value = argMap[i];
		html = html.replace(key, value);
	}
	return html;
}

function tobeImpl() {
	alert('未实现，敬请期待！');
	return false;
}

function formatFromTimestamp(timestamp) {
	var diff = new Date().getTime() - Number(timestamp);

	if (diff <= 1000 * 60 * 60) {
		// 60分钟内
		var min = parseInt(diff / (1000 * 60));
		if (min < 1) {
			min = 1;
		}
		return "" + min + "分钟前";
		
	} else if (diff <= 1000 * 60 * 60 * 24) {
		// 24小时内
		return "" + parseInt(diff / (1000 * 60 * 60)) + "小时前";
		
	} else if (diff <= 1000 * 60 * 60 * 24 * 7) {
		// 7天内
		return "" + parseInt(diff / (1000 * 60 * 60 * 24)) + "天前";
		
	} else if (diff <= 1000 * 60 * 60 * 24 * 7 * 4) {
		// 4个星期内
		return "" + parseInt(diff / (1000 * 60 * 60 * 24 * 7 )) + "个星期前";
		
	} else if (diff <= 1000 * 60 * 60 * 24 * 30 * 12) {
		// 12个月内
		return "" + parseInt(diff / (1000 * 60 * 60 * 24 * 30)) + "个月前";
		
	} else {
		// X年
		return "" + parseInt(diff / (1000 * 60 * 60 * 24 * 30 * 12)) + "年前";
	}
}

function convertStatus163(status) {
	var pattern = /http:\/\/[a-zA-Z0-9-\.]+\/[a-zA-Z0-9-]+/g;
	var result = status.match(pattern);
	var img = null;
	if (null != result) {
		var newContent = null;
		for (var i in result) {
			if (isNaN(i)) {
				continue;
			}
			var m = result[i];
			if (m.indexOf('http://126.fm') != -1) {
				img = m;
				newContent = '';
			} else {
				newContent = "<a title='原链有风险，点击需谨慎' target='_blank' href='" + m + "'>" + m + "</a>";
			}
			status = status.replace(m, newContent);
		}
	}
	
	return {'status':status, 'img':img};
}

function convertStatusSina(status) {
	return convertStatus163(status);
}

function convertStatusSohu(status) {
	return convertStatus163(status);
}

//function convertStatusQQ(status) {
//	// TODO
//	return {'status':status, 'img':""};
//}

function sortLocalWeiboList(list) {
	var listLen = list.length;
	for (var ii = 0; ii < listLen; ii++) {
		var swap = false;
		for (var jj = listLen - 1; jj > ii; jj--) {
			if (Number(list[jj].timestamp) > Number(list[jj-1].timestamp)) {
				swap = true;
				var tmp = list[jj];
				list[jj] = list[jj-1];
				list[jj-1] = tmp;
			}
		}
		if (!swap) {
			break;
		}
	}
	return list;
}

var timerHomeTimeline = null;

function loadHomeTimeline(url, argMap) {
	$("#moreBtn").attr('value','正在加载数据...').attr('disabled',true);
	if (!argMap) {
		argMap = {};
	}
	//WEIBO_MAP = {};
	$.post(url, argMap, function(data) {
		if (timerHomeTimeline) {
			clearInterval(timerHomeTimeline);
		}
		if (data) {
			var weiboList = data;
			var htmlArray = [];
			var localWeiboList = [];
			var sinaStatusCount = {};
			var sohuStatusCount = {};
			for (var i in weiboList) {
				var weibo = weiboList[i];
				var weiboId = weibo['weiboId'];
				var weiboAccountName = weibo['weiboAccountName'];
				var list = weibo['list'];
				if (weiboId.substring(0, 4) == "T_QQ") {
					if (list['data'] && 'ok' == list['msg']) {
						list = list['data']['info'];
					} else {
						list = [];
					}
				}
				
				for (var j in list) {
					var theWeibo = list[j];
					var statusId = null;
					var userHeader = null;
					var userName = null;
					var status = null;
					var statusImg = null;
					var statusRef = null;
					var statusRefUserName = null;
					var timestamp = null;
					var from = null;
					var retweetCount = null;
					var commentsCount = null;
					
					if (weiboId.substring(0, 5) == "T_163") {
						statusId = theWeibo['id'];
						userHeader = theWeibo['user']['profile_image_url'];
						userName = theWeibo['user']['name'];
						var statusObj = convertStatus163(theWeibo['text']);
						status = statusObj['status'];
						statusImg = statusObj['img'];
						statusRef = theWeibo['root_in_reply_to_status_text'] || "";
						statusRefUserName = theWeibo['root_in_reply_to_user_name'] || "";
						if (statusRef && "" != statusRef) {
							statusRef = convertStatus163(statusRef)['status'];
						}
						var createAt = theWeibo['created_at'];
						var createAtArray = createAt.split(" ");
						if (6 != createAtArray.length) {
							// TODO
							continue;
						}
						createAt = createAtArray[0] + " " + createAtArray[1] + " " + createAtArray[2] + " " + createAtArray[5] + " " + createAtArray[3] + " GMT" + createAtArray[4];
						timestamp = new Date(createAt).getTime();
						from = "<img src='" + getContext() + "/css/from163.png' title='内容来自网易微博[" + weiboAccountName + "]' />";
						retweetCount = theWeibo['retweet_count'];
						commentsCount = theWeibo['comments_count'];
					
					} else if (weiboId.substring(0, 6) == "T_SINA") {
						statusId = theWeibo['id'];
						
						if (WEIBO_MAP[weiboId] && WEIBO_MAP[weiboId]['max_id'] && statusId == WEIBO_MAP[weiboId]['max_id']) {
							// 剔除重复的
							continue;
						}
						
						userHeader = theWeibo['user']['profile_image_url'];
						userName = theWeibo['user']['name'];
						var statusObj = convertStatusSina(theWeibo['text']);
						status = statusObj['status'];
						statusImg = (theWeibo['thumbnail_pic'] || "");
						statusRef = "";
						statusRefUserName = "";
						if (theWeibo['retweeted_status']) {
							statusRef = convertStatusSina(theWeibo['retweeted_status']['text'])['status'];
							statusRefUserName = theWeibo['retweeted_status']['user']['name'];
						}
						var createAt = theWeibo['created_at'];
						var createAtArray = createAt.split(" ");
						if (6 != createAtArray.length) {
							// TODO
							continue;
						}
						createAt = createAtArray[0] + " " + createAtArray[1] + " " + createAtArray[2] + " " + createAtArray[5] + " " + createAtArray[3] + " GMT" + createAtArray[4];
						timestamp = new Date(createAt).getTime();
						from = "<img src='" + getContext() + "/css/fromsina.png' title='内容来自新浪微博[" + weiboAccountName + "]' />";
						retweetCount = "";
						commentsCount = "";
						if (!sinaStatusCount[weiboId]) {
							sinaStatusCount[weiboId] = "";
						}
						sinaStatusCount[weiboId] += (statusId + ",");
						
					} else if (weiboId.substring(0, 4) == "T_QQ") {
						statusId = theWeibo['id'];
						userHeader = theWeibo['head'];
						userName = theWeibo['nick'];
//						var statusObj = convertStatusQQ(theWeibo['text']);
//						status = statusObj['status'];
						status = theWeibo['text'];
						statusImg = "";
						if (theWeibo['image'] && theWeibo['image'].length > 0) {
							statusImg = theWeibo['image'][0];
						}
						statusRef = "";
						statusRefUserName = "";
						if (theWeibo['source']) {
							statusRef = theWeibo['source']['text'];
							statusRefUserName = theWeibo['source']['nick'];
						}
						timestamp = Number(theWeibo['timestamp']) * 1000;
						from = "<img src='" + getContext() + "/css/fromqq.png' title='内容来自腾讯微博[" + weiboAccountName + "]' />";
						retweetCount = "";
						commentsCount = theWeibo['count'];
						
					} else if (weiboId.substring(0, 6) == "T_SOHU") {
						statusId = theWeibo['id'];
						userHeader = theWeibo['user']['profile_image_url'];
						userName = theWeibo['user']['screen_name'];
						var statusObj = convertStatusSohu(theWeibo['text']); 
						status = statusObj['status'];
						statusImg = (theWeibo['middle_pic'] || "");
						statusRef = theWeibo['in_reply_to_status_text'] || "";
						statusRefUserName = theWeibo['in_reply_to_screen_name'] || "";
						if (statusRef && "" != statusRef) {
							statusRef = convertStatusSohu(statusRef)['status'];
						}
						var createAt = theWeibo['created_at'];
						var createAtArray = createAt.split(" ");
						if (6 != createAtArray.length) {
							// TODO
							continue;
						}
						createAt = createAtArray[0] + " " + createAtArray[1] + " " + createAtArray[2] + " " + createAtArray[5] + " " + createAtArray[3] + " GMT" + createAtArray[4];
						timestamp = new Date(createAt).getTime();
						from = "<img src='" + getContext() + "/css/fromsohu.png' title='内容来自搜狐微博[" + weiboAccountName + "]' />";
						retweetCount = "";
						commentsCount = "";
						if (!sohuStatusCount[weiboId]) {
							sohuStatusCount[weiboId] = "";
						}
						sohuStatusCount[weiboId] += (statusId + ",");
					
					} else {
						continue;
					}
					
					localWeiboList.push({
						'weiboId' : weiboId,
						'statusId' : statusId,
						'userHeader' : userHeader,
						'userName' : userName,
						'status' : status,
						'statusImg' : statusImg,
						'statusRef' : statusRef,
						'statusRefUserName' : statusRefUserName,
						'timestamp' : timestamp,
						'from' : from,
						'retweetCount' : retweetCount,
						'commentsCount' : commentsCount
					});
					
				}
				
				if (!WEIBO_MAP[weiboId]) {
					WEIBO_MAP[weiboId] = {'weiboId' : weiboId};
				}
				
				// TODO 整理weiboId的nextId
				if (weiboId.substring(0, 5) == "T_163") {
					// 163的返回此条索引之前发的微博列表,不包含此条
					if (0 == list.length) {
						WEIBO_MAP[weiboId]['since_id'] = null;
					} else {
						WEIBO_MAP[weiboId]['since_id'] = list[list.length-1]['cursor_id'];
					}
					
				} else if (weiboId.substring(0, 4) == "T_QQ") {
					if (0 == list.length) {
						WEIBO_MAP[weiboId]['pagetime'] = null;
					} else {
						WEIBO_MAP[weiboId]['pagetime'] = list[list.length-1]['timestamp'];
					}
					
				} else if (weiboId.substring(0, 6) == "T_SINA") {
					// sina的返回ID小于或等于max_id的微博消息
					if (0 == list.length) {
						WEIBO_MAP[weiboId]['max_id'] = null;
					} else {
						WEIBO_MAP[weiboId]['max_id'] = list[list.length-1]['id'];
					}
				} else if (weiboId.substring(0, 6) == "T_SOHU") {
					// sohu ?
					if (0 == list.length) {
						WEIBO_MAP[weiboId]['max_id'] = null;
					} else {
						WEIBO_MAP[weiboId]['max_id'] = list[list.length-1]['id'];
					}
				}
			}
			
			localWeiboList = sortLocalWeiboList(localWeiboList);
			for (var i in localWeiboList) {
				var local = localWeiboList[i];
				
				var createTime = formatFromTimestamp(local.timestamp);
				
				htmlArray.push("<div class='item' id='", local.weiboId, "XXX", local.statusId, "'>");
				htmlArray.push("<div class='item-1'><div title='", local.userName, "' class='userHeader f-left'><img src='", local.userHeader, "' /></div>");
				
				htmlArray.push("<div class='userItem f-right'><div class='userStatus'>");
				htmlArray.push("<div class='status-0'><div class='f-left'><span title='", local.userName, "' class='userName'>", local.userName, "</span></div><div class='time f-right'><span class='gray timestamp' timestamp='", local.timestamp, "'>", createTime, "</span></div>");
				if (local.statusImg && "" != local.statusImg) {
					htmlArray.push("<div imgSrc='", local.statusImg, "' title='图片' class='itisimg f-right' onmouseover='viewStatusImg(this);' onmouseout='finishViewStatusImg(this);'><img src='" + getContext() + "/css/itisimg.PNG' /></div>");
				}
				htmlArray.push("</div><div class='status-1'>", local.status, "</div>");
				if ("" != local.statusRef && "" != local.statusRefUserName) {
					var refUserNameSpan = "<span title='" + local.statusRefUserName + "' class='userName'>" + local.statusRefUserName + ":</span>";
					htmlArray.push("<div class='status-2'>", refUserNameSpan, local.statusRef, "</div>");
				}
				htmlArray.push("</div>");
				
				htmlArray.push("<div class='fromAndOpe'><div class='fromImg f-left'>", local.from, "</div>");
				
				var retweetNum = "";
				var commentsNum = "";
				if (local.retweetCount && "" != local.retweetCount && 0 != Number(local.retweetCount)) {
					retweetNum = "(" + local.retweetCount + ")";
				}
				if (local.commentsCount && "" != local.commentsCount && 0 != Number(local.commentsCount)) {
					commentsNum = "(" + local.commentsCount + ")";
				}
				htmlArray.push("<div class='operate f-right'><a class='aMarginRight' href='javascript:void(0);' onclick='statusesRetweet(this);'>转发", retweetNum, "</a><a class='aMarginRight' href='javascript:void(0);' onclick='statusesReply(this);'>评论", commentsNum, "</a><a class='aMarginRight' href='javascript:void(0);' onclick='favoritesCreate(this);'>收藏</a></div>");
				
				htmlArray.push("</div></div></div></div>");
			}
			
			$("#items").append(htmlArray.join(""));
			
			timerHomeTimeline = setInterval(function() {
				$(".timestamp").each(function() {
					var timestamp = $(this).attr("timestamp");
					$(this).html(formatFromTimestamp(timestamp));
				});
			}, 1000 * 10);
			
			var countSina = 0;
			for (var i in sinaStatusCount) {
				countSina += 1;
				sinaStatusCount[i] = sinaStatusCount[i].substring(0, sinaStatusCount[i].length-1);
			}
			if (countSina > 0) {
				$.post(getContext() + '/execute.do?api=sinaStatusesCounts',sinaStatusCount,function(data) {
					if (data) {
						var sinaList = data;
						for (var i in sinaList) {
							var sinaCountObj = sinaList[i];
							var weiboId = sinaCountObj['weiboId'];
							var list = sinaCountObj['list'];
							for (var j in list) {
								var statusId = list[j]['id'];
								var retweetCount = list[j]['rt'];
								var commentsCount = list[j]['comments'];
								if (retweetCount > 0) {
									$("#" + weiboId + "XXX" + statusId + " .operate a:eq(0)").html("转发(" + retweetCount + ")");
								}
								if (commentsCount > 0) {
									$("#" + weiboId + "XXX" + statusId + " .operate a:eq(1)").html("评论(" + commentsCount + ")");
								}
							}
						}
					}
				}, 'json');
			}
			
			var countSohu = 0;
			for (var i in sohuStatusCount) {
				countSohu += 1;
				sohuStatusCount[i] = sohuStatusCount[i].substring(0, sohuStatusCount[i].length-1);
			}
			if (countSohu > 0) {
				$.post(getContext() + '/execute.do?api=sohuStatusesCounts',sohuStatusCount,function(data) {
					if (data) {
						var sohuList = data;
						for (var i in sohuList) {
							var sohuCountObj = sohuList[i];
							var weiboId = sohuCountObj['weiboId'];
							var list = sohuCountObj['list'];
							for (var j in list) {
								var statusId = list[j]['id'];
								var retweetCount = list[j]['transmit_count'];
								var commentsCount = list[j]['comments_count'];
								if (retweetCount > 0) {
									$("#" + weiboId + "XXX" + statusId + " .operate a:eq(0)").html("转发(" + retweetCount + ")");
								}
								if (commentsCount > 0) {
									$("#" + weiboId + "XXX" + statusId + " .operate a:eq(1)").html("评论(" + commentsCount + ")");
								}
							}
						}
					}
				}, 'json');
			}
		}
		
		$("#moreBtn").attr('value','查看更多').attr('disabled',false);
		
	}, "json");
}

function initLoadHomeTimeline() {
	loadHomeTimeline(getContext() + '/execute.do?api=homeTimeline');
}

function moreHomeTimeline() {
	var postArg = {}
	for (var weiboId in WEIBO_MAP) {
		var weibo = WEIBO_MAP[weiboId];
		if (weibo['since_id']) {
			postArg[weiboId] = weibo['since_id'];
		} else if (weibo['max_id']) {
			postArg[weiboId] = weibo['max_id'];
		} else if (weibo['pagetime']) {
			postArg[weiboId] = weibo['pagetime'];
		} else {
			// TODO
		}
	}
	var count = 0;
	for (var i in postArg) {
		count++;
	}
	if (count > 0) {
		loadHomeTimeline(getContext() + '/execute.do?api=moreHomeTimeline', postArg);
	} else {
		$("#moreItem").hide();
		tipSuccess('没有更多的信息了!');
	}
}

function tip(msg) {
	alert(msg);
}

function tipError(msg) {
	$.growlUI('出错啦!!!', msg);
}

function tipSuccess(msg) {
	$.growlUI(null, msg);
}

function statusesUpdate() {
	var status = $.trim($("#statusTextarea").val());
	if (status && status.length > 0) {
		if (status.length > 0 && status.length <= 140) {
			$.post(getContext() + '/execute.do?api=statusesUpdate', {
				'status' : status
			}, function(data) {
				if (data && 'true' == data['status']) {
					tipSuccess("成功发布微博");
				} else {
					tipError("发布微博失败[" + data['desc'] + "]");
				}
			}, 'json');
			resetTextarea();
		} else if (status.length < 1) {
			resetTextarea();
			tipError("未输入内容！");
		} else {
			tipError("已经超出" + Math.abs(140 - status.length) + "个字");
		}
	}
}

function statusTextareaChange() {
	var status = $.trim($("#statusTextarea").val());
	if (status && status.length > 0) {
		var c = 140 - status.length;
		if (c >= 0) {
			$("#remainWordCount").css('color','black').html("还可以输入" + c + "个字");
		} else {
			$("#remainWordCount").css('color','red').html("已经超出" + Math.abs(c) + "个字");
		}
	}
}

function resetTextarea() {
	$("#statusTextarea").val("");
	$("#remainWordCount").css('color','black').html("还可以输入140个字");
}

function statusUpdate() {
	var status = $.trim($("#statusTextarea").val());
	alert("status:" + status);
	resetTextarea();
}

function getWeiboIdAndStatusId(tar) {
	var topDiv = $(tar).parent().parent().parent().parent().parent();
	var ids = topDiv.attr("id").split('XXX');
	var weiboId = ids[0];
	var statusId = ids[1];
	return {'weiboId':weiboId, 'statusId':statusId};
}

function statusesRetweet(tar) {
	var ws = getWeiboIdAndStatusId(tar);
	$.post(getContext() + '/execute.do?api=statusesRetweet', {
		'weiboId' : ws.weiboId,
		'statusId' : ws.statusId
	}, function(data) {
		if (data && 'true' == data['status']) {
			tipSuccess("成功转发微博");
		} else {
			tipError("转发微博失败[" + data['desc'] + "]");
		}
	}, 'json');
}

function inputTxtClick(tar) {
	var obj = $(tar);
	var value = $.trim(obj.val());
	var gValue = obj.attr("gValue");
	if (gValue == value) {
		obj.val("");
		obj.css("color","black");
	}
}

function inputTxtBlur(tar) {
	var obj = $(tar);
	var value = $.trim(obj.val());
	if ("" == value) {
		obj.val(obj.attr("gValue"));
		obj.css("color","gray");
	}
}

function moreReplys() {
	var tar = $("#rMoreStatusBtn");
	if (tar.length > 0) {
		var weiboId = tar.attr("weiboId");
		var statusId = tar.attr("statusId");
		var postCommentsArg = {
			'weiboId' : weiboId,
			'statusId' : statusId
		};
		
		var firstShow = true;
		var sinaSohuPage = null;
		if (weiboId.substring(0, 5) == "T_163") {
			if (tar.attr("since_id")) {
				firstShow = false;
				postCommentsArg['since_id'] = tar.attr("since_id");
			} 
		} else if (weiboId.substring(0, 4) == "T_QQ") {
			if (tar.attr("pagetime")) {
				firstShow = false;
				postCommentsArg['pagetime'] = tar.attr("pagetime");
				postCommentsArg['pageflag'] = 1;
			} 
		} else if (weiboId.substring(0, 6) == "T_SINA" || weiboId.substring(0, 6) == "T_SOHU") {
			if (tar.attr("page")) {
				firstShow = false;
				sinaSohuPage = Number(tar.attr("page")) + 1;
				postCommentsArg['page'] = tar.attr("page");
			} else {
				sinaSohuPage = 2;
			}
		} else {
			return;
		}
		
		$.post(getContext() + '/execute.do?api=statusesComments', postCommentsArg, function(data) {
			if (data) {
				var weiboList = data;
				if (weiboList.length > 0) {
					var list = weiboList[0]['list'];
					if (weiboId.substring(0, 4) == "T_QQ") {
						if (list['data'] && 'ok' == list['msg']) {
							list = list['data']['info'];
						} else {
							list = [];
						}
					}
					
					if (list.length > 0) {
						var htmlArr = [];
						var lastSinceId = null;
						var lastPagetime = null;
						for (var j in list) {
							var theWeibo = list[j];
							var text = null;
							var userName = null;
							if (weiboId.substring(0, 5) == "T_163") {
								lastSinceId = theWeibo['id'];
								text = theWeibo['text'];
								userName = theWeibo['user']['name'];
							} else if (weiboId.substring(0, 4) == "T_QQ") {
								lastPagetime = theWeibo['timestamp'];
								if (lastPagetime == tar.attr("pagetime")) {
									if (list.length > 1) {
										continue;
									} else {
										$("#rMoreStatusBtn").hide();
										return;
									}
								}
								text = theWeibo['text'];
								if ("" == text && 2 == theWeibo['type']) {
									text = "转发微博。";
								}
								userName = theWeibo['nick'];
							} else if (weiboId.substring(0, 6) == "T_SINA") {
								text = theWeibo['text'];
								userName = theWeibo['user']['name'];
							} else if (weiboId.substring(0, 6) == "T_SOHU") {
								text = theWeibo['text'];
								userName = theWeibo['user']['screen_name'];
							} else {
								continue;
							}
							// <div class='rStatus'><span class='rUser'>哈哈哈2：</span>xxxxxxxxx2</div>
							htmlArr.push("<div class='rStatus' title='", userName, "'><span class='rUser'>", userName, ":</span>", text, "</div>");
						}
						
						$(htmlArr.join("")).insertBefore("#rMoreStatusBtn");
						
						var replyStatusDiv = $("#replyStatusDiv");
						if (replyStatusDiv.height() > 300) {
							replyStatusDiv.addClass("replyStatusDivScroll");
						}
						
						if (lastSinceId) {
							tar.attr("since_id", lastSinceId);
						}
						
						if (lastPagetime) {
							tar.attr("pagetime", lastPagetime);
						}
						
						if (sinaSohuPage) {
							tar.attr("page", sinaSohuPage);
						}
						
						if (list.length < 19) {
							// 每页获取20条信息，如果少于19条，应该就是没有数据了的
							$("#rMoreStatusBtn").hide();
						}
						
						if (firstShow) {
							$("#replyStatusDiv").show(500);
						}
					} else {
						// 没有信息了
						$("#rMoreStatusBtn").hide();
					}
					
				} else {
					tipError("未能加载评论数据！");
				}
			} else {
				tipError("未能加载评论数据！");
			}
		}, 'json');
	}
}

function statusesReply(tar) {
	var ws = getWeiboIdAndStatusId(tar);
	var html = "<div id='replyStatusDiv'>" +
			"<div id='rMoreStatusDiv' class='rMoreStatus'><input id='rMoreStatusBtn' weiboId='" + ws.weiboId + "' statusId='" + ws.statusId + "' class='rMoreBtn' type='button' value='查看更多评论' onclick='moreReplys();' /></div>" +
			"</div><textarea onclick='inputTxtClick(this);' onblur='inputTxtBlur(this);' gValue='点击输入评论' class='rArea' name='userReplyContent'>点击输入评论</textarea>";
	$.prompt(html,{
		callback: function(v, m, f){
			if(v != undefined && 'submit' == v) {
				var reply = $.trim(f.userReplyContent);
				if (reply && '' != reply && '点击输入评论' != reply) {
					$.post(getContext() + '/execute.do?api=statusesReply', {
						'weiboId' : ws.weiboId,
						'statusId' : ws.statusId,
						'status' : reply
					}, function(data) {
						if (data && 'true' == data['status']) {
							tipSuccess("成功发布评论");
						} else {
							tipError("发布评论失败[" + data['desc'] + "]");
						}
					}, 'json');
				} else {
					tipError("发布评论失败，未输入评论内容！");
				}
			}
		},
		buttons: { 发布: 'submit', 取消:'cancel' }
	});
	
	moreReplys();
}

function favoritesCreate(tar) {
	var ws = getWeiboIdAndStatusId(tar);
	$.post(getContext() + '/execute.do?api=favoritesCreate', {
		'weiboId' : ws.weiboId,
		'statusId' : ws.statusId
	}, function(data) {
		if (data && 'true' == data['status']) {
			tipSuccess("成功收藏微博");
		} else {
			tipError("收藏微博失败[" + data['desc'] + "]");
		}
	}, 'json');
}

function viewStatusImg(tar) {
	//return tobeImpl();
}

function finishViewStatusImg(tar) {
	//return tobeImpl();
}

function initMainPage() {
	initLoadHomeTimeline();
}

function statusesFlush() {
	$("#items").html("");
	initMainPage();
}

