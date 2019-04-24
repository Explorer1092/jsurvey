(function($) {
	$.fn.tree = function(opt, nodes) {
		var setting = {};
		$.extend(setting, opt);
		var treeJq = $(this);
		var treeId = treeJq.attr("id");
		var tree = getTreeObj(treeId);
		if (tree == null) {
			var getFont = function(treeId, node) {
				return node.font ? node.font : {};
			};
			var options = {
				view : {
					fontCss : getFont,
					showLine : false,
					nameIsHTML : true,
					selectedMulti : false,
					addDiyDom : null,
					expandSpeed : '',
					fitWidth : true,
					fitContainer : treeJq.parent()
				},
				data : {
					simpleData : {
						enable : true
					}
				},
				check: {
					enable: false
				}
			};

			if (setting) {
				if (setting.view) {
					$.extend(options.view, setting.view);
					delete setting.view;
				}
				if (setting.check) {
					$.extend(options.check, setting.check);
					delete setting.check;
				}
			}

			if (options.view.fitWidth && options.check.enable == false) {
				// 适应宽度
				options.view.fitContainer.scroll(function() {
					fitWidth();
				});

				options.callback = {
					onExpand : toggleNode,
					onCollapse : toggleNode,
					onDblClick : toggleNode
				};
				if (setting && setting.callback) {
					$.extend(options.callback, setting.callback);
					delete setting.callback;
				}

				options.view.addDiyDom = addDiyDom;
			}

			options = $.extend(options, setting);
			options.view.showLine = false;
			tree = $.fn.zTree.init(this, options, nodes);

			//加适应宽度标记
			if (options.view.fitWidth && options.check.enable == false) {
				treeJq.addClass('fitWidth');
			}
			/** ********************树节点行选*************************** */
			function addDiyDom(treeId, treeNode) {
				var spaceWidth = 15;
				var switchObj = $("#" + treeNode.tId + "_switch"), icoObj = $("#" + treeNode.tId + "_ico");
				switchObj.remove();
				icoObj.before(switchObj);

				var spaceStr = "<div style='display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></div>";
				switchObj.before(spaceStr);
			}

			function toggleNode() {
				$('#tree').width('auto');
				fitWidth();
			}

			function fitWidth() {
				var scrollWidth = options.view.fitContainer[0].scrollWidth;
				var width = options.view.fitContainer.width();
				if (width == scrollWidth) {
					return;
				}

				treeJq.width(scrollWidth);
			}
			/** ********************树节点行选END*************************** */
		}
		var treeObj = bindMethod(tree);
		return treeObj;
	};

	function bindMethod(tree) {
		var treeObj = {
			getTree : function() {
				return tree;
			},
			expandAll : function(expand) {
				tree.expandAll(expand);
				return this;
			},
			addNodes : function(nodes) {
				if (typeof (nodes) == 'string') {
					nodes = $.parseJSON(nodes);
				}
				if (nodes) {
					var pNode, pid;
					if ($.isArray(nodes)) {
						$(nodes).each(function(element) {
							pid = this.pId;
							pNode = getNodeById(pid, tree);
							tree.addNodes(pNode, this);
						});
					} else {
						pid = nodes.pId;
						pNode = getNodeById(pid, tree);
						tree.addNodes(pNode, nodes);
					}
				}
				return this;
			},
			removeNode : function(ids, prefix, suffix) {
				if (ids) {
					var nodeIds = ids.split(',');
					$.each(nodeIds, function(index, nodeId) {
						if(prefix){
							nodeId = prefix+nodeId;
						}
						if(suffix){
							nodeId = nodeId+suffix;
						}
						var node = getNodeById(nodeId, tree);
						tree.removeNode(node);
					});
				}
				return this;
			},
			updateNode : function(nodes) {
				if (typeof (nodes) == 'string') {
					nodes = $.parseJSON(nodes);
				}
				if (nodes) {
					var currNode;
					if ($.isArray(nodes)) {
						$.each(nodes, function(index, node) {
							currNode = getNodeById(node.id, tree);
							currNode = $.extend(currNode, node);
							tree.updateNode(currNode);
						});
					} else {
						currNode = getNodeById(nodes.id, tree);
						currNode = $.extend(currNode, nodes);
						tree.updateNode(currNode);
					}
				}
				return this;
			},
			refresh : function() {
				tree.refresh();
			},
			refreshNode : function(id, refreshType, isSilent) {
				if (id) {
					var node = getNodeById(id, tree);
					if (node) {
						node.isParent = true;
						tree.reAsyncChildNodes(node, 'refresh', isSilent);
					}
				}
				return this;
			},
			getAttr : function(id, key) {
				if (id) {
					var node = getNodeById(id, tree);
					if (node) {
						return node.attr[key];
					}
				}
			}
		};
		return treeObj;
	}

	function getTreeObj(obj) {
		return $.fn.zTree.getZTreeObj(obj);
	}

	function getNodeById(id, tree) {
		if (id == null) {
			return null;
		}
		return tree.getNodeByParam("id", id);
	}
})(jQuery);