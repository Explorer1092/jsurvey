/*
 * jQuery.validity ﻿v1.3.1
 * http://validity.thatscaptaintoyou.com/
 * https://github.com/whatgoodisaroad/validity
 * 
 * Dual licensed under MIT and GPL
 *
 * Date: 2013-02-10 (Sunday, 10 February 2013)
 */
(function($, undefined) {

	// Default settings:
	// /////////////////////////////////////////////////////////////////////////////

	var defaults = {
		// The default output mode is tooltip because it requires no
		// dependencies:
		outputMode : "tooltip",

		// The this property is set to true, validity will scroll the browser
		// viewport so that the first error is visible when validation fails:
		scrollTo : false,

		// If this setting is true, modal errors will disappear when they are
		// clicked on:
		modalErrorsClickable : true,

		// If a field name cannot be otherwise inferred, this will be used:
		defaultFieldName : "This field",

		// jQuery selector to filter down to validation-supported elements:
		elementSupport : ":text, :password, textarea, select, :radio, :checkbox, input[type='hidden'], input[type='tel'], input[type='email']",

		// Function to stringify argments for use when generating error
		// messages. Primarily, it just generates pretty date strings:
		argToString : function(val) {
			return val.getDate ? [ val.getMonth() + 1, val.getDate(), val.getFullYear() ].join("/") : val + "";
		},

		debugPrivates : false
	},

	__private;

	// Static functions and properties:
	// /////////////////////////////////////////////////////////////////////////////

	$.validity = {

		// Clone the defaults. They can be overridden with the setup function:
		settings : $.extend(defaults, {}),

		// Built-in library of format-checking tools for use with the match
		// validator as well as the nonHtml validator:
		patterns : {
			intege1 : /^[1-9]\d*$/, // 正整数
			intege2 : /^-[1-9]\d*$/, // 负整数
			num : /^([+-]?)\d*\.?\d+$/, // 数字
			num1 : /^[0-9]\d*$/, // 正数（正整数 + 0）
			num2 : /^-[0-9]\d*$/, // 负数（负整数 + 0）
			decmal : /^([+-]?)\d*\.\d+$/, // 浮点数
			decmal1 : /^[1-9]\d*.\d*|0.\d*[1-9]\d*$/,// 正浮点数
			decmal2 : /^-([1-9]\d*.\d*|0.\d*[1-9]\d*)$/,// 负浮点数
			decmal3 : /^-?([1-9]\d*.\d*|0.\d*[1-9]\d*|0?.0+|0)$/,// 浮点数
			decmal4 : /^([0-9]\d*\.\d+|0)$/,// 非负浮点数（正浮点数 + 0）
			decmal5 : /^(-([1-9]\d*.\d*|0.\d*[1-9]\d*))|0?.0+|0$/,// 非正浮点数（负浮点数 + 0）

			email : /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/, // 邮件
			color : /^[a-fA-F0-9]{6}$/, // 颜色
			chinese : /^[\u4E00-\u9FA5\uF900-\uFA2D]+$/, // 仅中文
			ascii : /^[\x00-\xFF]+$/, // 仅ACSII字符
			zipcode : /^\d{6}$/, // 邮编
			mobile : /^[1][3,4,5,6,7,8][0-9]{9}$/, // 手机
			ip4 : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/, // ipv4地址
			ip6 : /^((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?$/, // ipv6地址
			picture : /(.*)\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$/, // 图片
			rar : /(.*)\.(rar|zip|7zip|tgz)$/, // 压缩文件
			zip : /(.*)\.(zip)$/,
			qq : /^[1-9]*[1-9][0-9]*$/, // QQ号码
			tel : /(\d{3}-|\d{4}-)?(\d{8}|\d{7})/, // 国内电话
			username : /^\w+$/, // 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
			username1 : /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,// 用来用户注册。匹配字母、数字、下划线、中文组成，不能以下划线开头和结尾
			letter : /^[A-Za-z]+$/, // 字母
			letter_u : /^[A-Z]+$/, // 大写字母
			letter_l : /^[a-z]+$/, // 小写字母
			codeid : /^[a-zA-Z0-9]+$/, // 机构标识。大小写字母及数字
			idcard : /^[1-9]([0-9]{14}|([0-9]{16}([xX]|\d)))$/, // 身份证
			date : /^((0?\d)|(1[012]))[\/-]([012]?\d|30|31)[\/-]\d{1,4}$/,
			url : /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i,
			urlv6 : /^(https?|ftp):\/\/\[((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\]:?/i,
			nonHtml : /^[^<>]*$/
		},

		// Built-in set of default error messages (for use when a message isn't
		// specified):
		messages : {

			require : "#{field} is required.",

			// Format validators:
			match : "#{field} is in an invalid format.",
			integer : "#{field} must be a positive, whole number.",
			date : "#{field} must be formatted as a date. (mm/dd/yyyy)",
			email : "#{field} must be formatted as an email.",
			usd : "#{field} must be formatted as a US Dollar amount.",
			url : "#{field} must be formatted as a URL.",
			number : "#{field} must be formatted as a number.",
			zip : "#{field} must be formatted as a zipcode ##### or #####-####.",
			phone : "#{field} must be formatted as a phone number ###-###-####.",
			guid : "#{field} must be formatted as a guid like {3F2504E0-4F89-11D3-9A0C-0305E82C3301}.",
			time24 : "#{field} must be formatted as a 24 hour time: 23:00.",
			time12 : "#{field} must be formatted as a 12 hour time: 12:00 AM/PM",

			// Value range messages:
			lessThan : "#{field} must be less than #{max}.",
			lessThanOrEqualTo : "#{field} must be less than or equal to #{max}.",
			greaterThan : "#{field} must be greater than #{min}.",
			greaterThanOrEqualTo : "#{field} must be greater than or equal to #{min}.",
			range : "#{field} must be between #{min} and #{max}.",

			// Value length messages:
			tooLong : "#{field} cannot be longer than #{max} characters.",
			tooShort : "#{field} cannot be shorter than #{min} characters.",

			// Composition validators:
			nonHtml : "#{field} cannot contain HTML characters.",
			alphabet : "#{field} contains disallowed characters.",

			minCharClass : "#{field} cannot have more than #{min} #{charClass} characters.",
			maxCharClass : "#{field} cannot have less than #{min} #{charClass} characters.",

			// Aggregate validator messages:
			equal : "Values don't match.",
			distinct : "A value was repeated.",
			sum : "Values don't add to #{sum}.",
			sumMax : "The sum of the values must be less than #{max}.",
			sumMin : "The sum of the values must be greater than #{min}.",

			// Radio validator messages:
			radioChecked : "The selected value is not valid.",

			generic : "Invalid."
		},

		// Abstract output:
		out : {
			start : function() {
				this.defer("start");
			},
			end : function(results) {
				this.defer("end", results);
			},
			raise : function($obj, msg) {
				this.defer("raise", $obj, msg);
			},
			raiseAggregate : function($obj, msg) {
				this.defer("raiseAggregate", $obj, msg);
			},
			defer : function(name) {
				var v = $.validity, o = v.outputs[v.settings.outputMode];

				o[name].apply(o, Array.prototype.slice.call(arguments, 1));
			}
		},

		// Character classes can be used to determine the quantity of a given type
		// of character in a string:
		charClasses : {
			alphabetical : /\w/g,
			numeric : /\d/g,
			alphanumeric : /[A-Za-z0-9]/g,
			symbol : /[^A-Za-z0-9]/g
		},

		// Object to contain the output modes. The three built-in output modes are
		// installed later on in this script.
		outputs : {},

		// By default, private members are not exposed.
		__private : undefined,

		// Override the default settings with user-specified ones.
		setup : function(options) {
			this.settings = $.extend(this.settings, options);

			if (this.settings.debugPrivates) {
				this.__private = __private;
			} else {
				this.__private = undefined;
			}
		},

		// Object to store information about ongoing validation. When validation
		// starts, this will be set to a report object. When validators fail, they
		// will inform this object. When validation is completed, this object will
		// contain the information of whether it succeeded:
		report : null,

		// Determine whether validity is in the middle of validation:
		isValidating : function() {
			return !!this.report;
		},

		// Function to prepare validity to start validating:
		start : function() {

			// The output mode should be notified that validation is starting. This
			// usually means that the output mode will erase errors from the
			// document in whatever way the mode needs to:
			this.out.start();

			// Initialize the report object:
			this.report = {
				errors : 0,
				valid : true
			};
		},

		// Function called when validation is over to examine the results and
		// clean-up:
		end : function() {

			// Null coalescence: fix for Issue 5:
			var results = this.report || {
				errors : 0,
				valid : true
			};

			this.report = null;

			// Notify the current output mode that validation is over:
			this.out.end(results);

			return results;
		},

		// Remove validiation errors:
		clear : function() {
			this.start();
			this.end();
		}
	};

	// jQuery instance methods:
	// /////////////////////////////////////////////////////////////////////////////

	$.fn.extend({

		// The validity function is how validation can be bound to forms. The user
		// may pass in a validation function or, as a shortcut, pass in a string of
		// a CSS selector that grabs all the inputs to require:
		/**
		 * 
		 */
		validity : function(arg, setting) {

			return this.each(

			function() {
				var op = {
					auto : true,
					type : 'ajax',
					resultType : 'json',
					contentType : 'application/x-www-form-urlencoded',
					beforeSubmit : function(result) {
					},
					success : function() {
					},
					error : function() {
					}
				};
				op = $.extend(op, setting);
				// Only operate on forms:
				if (this.tagName.toLowerCase() == "form") {
					var form = $(this);
					var f = null;

					// If the user entered a string to select the inputs to
					// require, then make the validation logic ad hoc:
					if (typeof (arg) == "string") {
						f = function() {
							$(arg).require();
						};
					}

					// If the user entered a validation function then just call
					// that at the appropriate time:
					else if ($.isFunction(arg)) {
						f = arg;
					}

					if (arg) {
						if (op.auto == true && op.type == 'iframe') {
							$('body').append('<iframe frameborder="0" id="hiddenSubmit" name="hiddenSubmit" style="width: 0;height: 0"></iframe>');
							$(this).attr('target', 'hiddenSubmit');
						}
						$(this).bind("submit", function() {
							$.validity.start();
							f();
							var result = $.validity.end().valid;
							if(result){
								var before = op.beforeSubmit(result);
								if(before == false){
									return false;
								}
							}
							
							if (result == true && op.auto == true) {
								var submitBtn = $(form.find('[type=submit],.submit'));
								var data = form.serialize();
								submitBtn.attr('disabled', true).removeClass('btn-primary').addClass('disabled');
								if(op.type == 'ajax'){
									var url = form.attr('action');
									$.ajax({
										type : "post",
										url : url,
										data : data,
										dataType : op.resultType,
										contentType : op.contentType,
										success : function(obj) {
											submitBtn.attr('disabled', false).removeClass('disabled').addClass('btn-primary');
											op.success(obj);
										},
										error : function(a, b, msg) {
											submitBtn.attr('disabled', false).removeClass('disabled').addClass('btn-primary');
											alert('操作失败');
											op.error();
										}
									});
									result = false;
								}
							}
							return result;
						});
					}
				}
			});
		},

		// Validators:
		// /////////////////////////////////////////////////////////////////////////

		// Common validators:
		// /////////////////////////////////////////////////////////////////////////

		// Validate whether the field has a value.
		require : function(msg) {
			return validate(this, function(obj) {
				return !!$.trim($(obj).val()).length;
			}, msg || $.validity.messages.require);
		},

		// Validate whether the field matches a regex.
		match : function(rule, msg) {

			// If a default message is to be used:
			if (!msg) {
				// First grab the generic one:
				msg = $.validity.messages.match;

				// If there's a more specific one, use that.
				if (typeof (rule) === "string" && $.validity.messages[rule]) {
					msg = $.validity.messages[rule];
				}
			}
			// If the rule is named, rather than specified:
			if(rule == 'ip'){
				rule = function(value){
					if($.validity.patterns['ip4'].test(value) || $.validity.patterns['ip6'].test(value)){
						return true;
					}
					return false;
				}
			} else if(rule == 'url'){
				rule = function(value){
					if($.validity.patterns['url'].test(value) || $.validity.patterns['urlv6'].test(value)){
						return true;
					}
					return false;
				}
			} else if (typeof (rule) == "string") {
				rule = $.validity.patterns[rule];
			}

			return validate(this,

			// Some of the named rules can be functions, such as 'date'. If the
			// discovered rule is a function use it as such. Otherwise, assume
			// it's a RegExp.
			$.isFunction(rule) ?

			function(obj) {
				return !obj.value.length || rule(obj.value);
			} :

			function(obj) {
				// Fix for regexes where the global flag is set. Make sure
				// to test from the start of the string.
				if (rule.global) {
					rule.lastIndex = 0;
				}

				return !obj.value.length || rule.test(obj.value);
			},

			msg);
		},

		range : function(min, max, msg) {
			return validate(this,

			min.getTime && max.getTime ?

			// If both arguments are dates then use them that way.
			function(obj) {
				var d = new Date(obj.value);
				return d >= new Date(min) && d <= new Date(max);
			} :

			min.substring && max.substring && Big ?

			// If both arguments are strings then parse them using the
			// Arbitrary-Precision library.
			function(obj) {
				var n = new Big(obj.value);
				return (n.greaterThanOrEqualTo(new Big(min)) && n.lessThanOrEqualTo(new Big(max)));
			} :

			// Otherwise treat them like floats.
			function(obj) {
				var f = parseFloat(obj.value);
				return f >= min && f <= max;
			},

			msg || format($.validity.messages.range, {
				min : $.validity.settings.argToString(min),
				max : $.validity.settings.argToString(max)
			}));
		},

		greaterThan : function(min, msg) {
			return validate(this,

			min.getTime ? function(obj) {
				return new Date(obj.value) > min;
			} :

			min.substring && Big ?

			function(obj) {
				return new Big(obj.value).greaterThan(new Big(min));
			} :

			function(obj) {
				return parseFloat(obj.value) > min;
			},

			msg || format($.validity.messages.greaterThan, {
				min : $.validity.settings.argToString(min)
			}));
		},

		greaterThanOrEqualTo : function(min, msg) {
			return validate(this,

			min.getTime ? function(obj) {
				return new Date(obj.value) >= min;
			} :

			min.substring && Big ?

			function(obj) {
				return new Big(obj.value).greaterThanOrEqualTo(new Big(min));
			} :

			function(obj) {
				return parseFloat(obj.value) >= min;
			},

			msg || format($.validity.messages.greaterThanOrEqualTo, {
				min : $.validity.settings.argToString(min)
			}));
		},

		lessThan : function(max, msg) {
			return validate(this,

			max.getTime ? function(obj) {
				return new Date(obj.value) < max;
			} :

			max.substring && Big ?

			function(obj) {
				return new Big(obj.value).lessThan(new Big(max));
			} :

			function(obj) {
				return parseFloat(obj.value) < max;
			},

			msg || format($.validity.messages.lessThan, {
				max : $.validity.settings.argToString(max)
			}));
		},

		lessThanOrEqualTo : function(max, msg) {
			return validate(this,

			max.getTime ? function(obj) {
				return new Date(obj.value) <= max;
			} :

			max.substring && Big ?

			function(obj) {
				return new Big(obj.value).lessThanOrEqualTo(new Big(max));
			} :

			function(obj) {
				return parseFloat(obj.value) <= max;
			},

			msg || format($.validity.messages.lessThanOrEqualTo, {
				max : $.validity.settings.argToString(max)
			}));
		},

		maxLength : function(max, msg) {
			return validate(this, function(obj) {
				return byteLength(obj.value) <= max;
			}, msg || format($.validity.messages.tooLong, {
				max : max
			}));
		},

		minLength : function(min, msg) {
			return validate(this, function(obj) {
				return byteLength(obj.value) >= min;
			}, msg || format($.validity.messages.tooShort, {
				min : min
			}));
		},

		alphabet : function(alpha, msg) {
			var chars = [];

			return validate(this, function(obj) {

				// For each character in the string, ensure that it's in the
				// alphabet definition:
				for ( var idx = 0; idx < obj.value.length; ++idx) {
					if (alpha.indexOf(obj.value.charAt(idx)) == -1) {
						chars.push(obj.value.charAt(idx));
						return false;
					}
				}

				return true;
			}, msg || format($.validity.messages.alphabet, {
				chars : chars.join(", ")
			}));
		},

		minCharClass : function(charClass, min, msg) {
			if (typeof (charClass) == "string") {
				charClass = charClass.toLowerCase();

				if ($.validity.charClasses[charClass]) {
					charClass = $.validity.charClasses[charClass];
				}
			}

			return validate(this, function(obj) {
				return (obj.value.match(charClass) || []).length >= min;
			}, msg || format($.validity.messages.minCharClass, {
				min : min,
				charClass : charClass
			}));
		},

		maxCharClass : function(charClass, max, msg) {
			if (typeof (charClass) == "string") {
				charClass = charClass.toLowerCase();

				if ($.validity.charClasses[charClass]) {
					charClass = $.validity.charClasses[charClass];
				}
			}

			return validate(this, function(obj) {
				return (obj.value.match(charClass) || []).length <= max;
			}, msg || format($.validity.messages.maxCharClass, {
				max : max,
				charClass : charClass
			}));
		},

		// Validate that the input does not contain potentially dangerous strings.
		nonHtml : function(msg) {
			return validate(this,

			function(obj) {
				return $.validity.patterns.nonHtml.test(obj.value);
			},

			msg || $.validity.messages.nonHtml);
		},

		// Aggregate validators:
		// /////////////////////////////////////////////////////////////////////////

		// Validate that all matched elements bear the same values. Accepts a
		// function to transform the values for testing.
		equal : function(arg0, arg1) {
			var
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			$reduction = (this.reduction || this).filter($.validity.settings.elementSupport),

			transform = function(val) {
				return val;
			},

			msg = $.validity.messages.equal;

			if ($reduction.length) {

				// Figure out what arguments were specified.
				if ($.isFunction(arg0)) {
					transform = arg0;

					if (typeof (arg1) == "string") {
						msg = arg1;
					}
				}

				else if (typeof (arg0) == "string") {
					msg = arg0;
				}

				var map = $.map($reduction, function(obj) {
					return transform(obj.value);
				}),

				// Get the first value.
				first = map[0], valid = true;

				// If any value is not equal to the first value, then they aren't
				// all equal, and it's not valid.
				for ( var i in map) {
					if (map[i] != first) {
						valid = false;
					}
				}

				if (!valid) {
					raiseAggregateError($reduction, msg);

					// The set reduces to zero valid elements.
					this.reduction = $([]);
				}
			}

			return this;
		},

		// Validate that all matched elements bear distinct values. Accepts an
		// optional function to transform the values for testing.
		distinct : function(arg0, arg1) {
			var
			// If a reduced set is attached, use it.
			// Also, remove unsupported elements.
			$reduction = (this.reduction || this).filter($.validity.settings.elementSupport),

			transform = function(val) {
				return val;
			},

			msg = $.validity.messages.distinct,

			// An empty array to store already-examined values
			subMap = [],

			// An array with repeated values
			repeatedVal = [],

			valid = true;

			if ($reduction.length) {

				// Figure out what arguments were specified.
				if ($.isFunction(arg0)) {
					transform = arg0;

					if (typeof (arg1) == "string") {
						msg = arg1;
					}
				}

				else if (typeof (arg0) == "string") {
					msg = arg0;
				}

				// Map all the matched values into an array.
				var map = $.map($reduction, function(obj) {
					return transform(obj.value);
				});

				// For each transformed value:
				for ( var i1 = 0; i1 < map.length; ++i1) {
					// Unless it's an empty string:
					if (map[i1].length) {
						// For each value we've already looked at:
						for ( var i2 = 0; i2 < subMap.length; ++i2) {
							// If we've already seen the transformed value:
							if (subMap[i2] == map[i1]) {
								valid = false;
								// Collect repeated values
								repeatedVal.push(map[i1]);
							}
						}

						// We looked at the value.
						subMap.push(map[i1]);
					}
				}

				if (!valid) {
					// Remove duplicates of duplicates
					repeatedVal = $.unique(repeatedVal);

					// For all repeated values found...
					for ( var i = 0, repeatedLength = repeatedVal.length; i < repeatedLength; ++i) {

						// raise the error - aggregate will use last repeated value
						raiseAggregateError($reduction.filter("[value=\'" + repeatedVal[i] + "\']"), msg);
					}

					// The set reduces to zero valid elements.
					this.reduction = $([]);
				}
			}

			return this;
		},

		// Validate that the numeric sum of all values is equal to a given value.
		sum : function(sum, msg) {
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			var $reduction = (this.reduction || this).filter($.validity.settings.elementSupport);

			if ($reduction.length && sum != numericSum($reduction)) {
				raiseAggregateError($reduction, msg || format($.validity.messages.sum, {
					sum : sum
				}));

				// The set reduces to zero valid elements.
				this.reduction = $([]);
			}

			return this;
		},

		// Validates an inclusive upper-bound on the numeric sum of the values of
		// all matched elements.
		sumMax : function(max, msg) {
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			var $reduction = (this.reduction || this).filter($.validity.settings.elementSupport);

			if ($reduction.length && max < numericSum($reduction)) {
				raiseAggregateError($reduction, msg || format($.validity.messages.sumMax, {
					max : max
				}));

				// The set reduces to zero valid elements.
				this.reduction = $([]);
			}

			return this;
		},

		// Validates an inclusive lower-bound on the numeric sum of the values of
		// all matched elements.
		sumMin : function(min, msg) {
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			var $reduction = (this.reduction || this).filter($.validity.settings.elementSupport);

			if ($reduction.length && min > numericSum($reduction)) {
				raiseAggregateError($reduction, msg || format($.validity.messages.sumMin, {
					min : min
				}));

				// The set reduces to zero valid elements.
				this.reduction = $([]);
			}

			return this;
		},

		// Radio group validators:
		// /////////////////////////////////////////////////////////////////////////

		radioChecked : function(val, msg) {
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			var $reduction = (this.reduction || this).filter($.validity.settings.elementSupport);

			if ($reduction.is(":radio") && $reduction.find(":checked").val() != val) {
				raiseAggregateError($reduction, msg || $.validity.messages.radioChecked);
			}
		},

		radioNotChecked : function(val, msg) {
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			var $reduction = (this.reduction || this).filter($.validity.settings.elementSupport);

			if ($reduction.is(":radio") && $reduction.filter(":checked").val() == val) {
				raiseAggregateError($reduction, msg || $.validity.messages.radioChecked);
			}
		},

		// Checkbox validators:
		// /////////////////////////////////////////////////////////////////////////

		checkboxChecked : function(msg) {
			// If a reduced set is attached, use it. Also, remove unsupported
			// elements.
			var $reduction = (this.reduction || this).filter($.validity.settings.elementSupport);

			if ($reduction.is(":checkbox") && !$reduction.is(":checked")) {
				raiseAggregateError($reduction, msg || $.validity.messages.radioChecked);
			}
		},

		// Specialized validators:
		// /////////////////////////////////////////////////////////////////////////

		// If expression is a function, it will be called on each matched element.
		// Otherwise, it is treated as a boolean, and the determines the validity
		// of elements in an aggregate fashion.
		assert : function(expression, msg) {

			// If a reduced set is attached, use it. Do not reduce to supported
			// elements.
			var $reduction = this.reduction || this;

			if ($reduction.length) {

				// In the case that 'expression' is a function, use it as a
				// regimen on each matched element individually:
				if ($.isFunction(expression)) {
					return validate(this, expression, msg || $.validity.messages.generic);
				}

				// Otherwise map it to a boolean and consider this as an aggregate
				// validation:
				else if (!expression) {

					raiseAggregateError($reduction, msg || $.validity.messages.generic);

					// The set reduces to zero valid elements.
					this.reduction = $([]);
				}
			}

			return this;
		},

		fail : function(msg) {
			return this.assert(false, msg);
		}
	});

	// Private utilities:
	// /////////////////////////////////////////////////////////////////////////////

	// Do non-aggregate validation. Subject each element in $obj to the regimen.
	// Raise the specified error on failures. This function is the heart of
	// validity:
	function validate($obj, regimen, message) {

		var
		// If a reduced set is attached, use it Also, remove any unsupported
		// elements.
		$reduction = ($obj.reduction || $obj).filter($.validity.settings.elementSupport),

		// Array to store only elements that pass the regimen.
		elements = [];

		// For each in the reduction.
		$reduction.each(function() {
			// If the element passes the regimen, include it in the reduction.
			if (regimen(this)) {
				elements.push(this);
			}

			// Else give the element an error message.
			else {
				raiseError(this, format(message, {
					field : infer(this)
				}));
			}
		});

		// Attach a (potentially) reduced set of only elements that passed.
		$obj.reduction = $(elements);

		// Return the full set with attached reduction.
		return $obj;
	}

	// Inform the report object that there was a failure.
	function addToReport() {
		if ($.validity.isValidating()) {
			$.validity.report.errors++;
			$.validity.report.valid = false;
		}
	}

	// Inform the report of a failure and display an error according to the idiom
	// of the current output mode.
	function raiseError(obj, msg) {
		addToReport();

		$.validity.out.raise($(obj), msg);
	}

	// Inform the report of a failure and display an aggregate error according to
	// the idiom of the current output mode.
	function raiseAggregateError($obj, msg) {
		addToReport();

		$.validity.out.raiseAggregate($obj, msg);
	}

	// Yield the sum of the values of all fields matched in obj that can be parsed.
	function numericSum(obj) {
		var accumulator = 0;
		obj.each(function() {
			var n = parseFloat(this.value);

			accumulator += isNaN(n) ? 0 : n;
		});
		return accumulator;
	}

	// Using the associative array supplied as the 'obj' argument, replace tokens
	// of the format #{<key>} in the 'str' argument with that key's value.
	function format(str, obj) {
		for ( var p in obj) {
			if (obj.hasOwnProperty(p)) {
				str = str.replace(new RegExp("#\\{" + p + "\\}", "g"), obj[p]);
			}
		}
		return capitalize(str);
	}

	// Infer the field name of the passed DOM element. If a title is specified, its
	// value is returned. Otherwise, attempt to parse a field name out of the id
	// attribute. If that doesn't work, return the default field name in the
	// configuration.
	function infer(field) {
		var $f = $(field), id = $f.prop("id"), ret = $.validity.settings.defaultFieldName;

		// Check for title.
		if ($f.prop("title").length) {
			ret = $f.prop("title");
		}

		// Check for UpperCamelCase.
		else if (/^([A-Z0-9][a-z]*)+$/.test(id)) {
			ret = id.replace(/([A-Z0-9])[a-z]*/g, " $&");
		}

		// Check for lowercase_separated_by_underscores
		else if (/^[a-z0-9]+(_[a-z0-9]+)*$/.test(id)) {
			var arr = id.split("_");

			for ( var i = 0; i < arr.length; ++i) {
				arr[i] = capitalize(arr[i]);
			}

			ret = arr.join(" ");
		}

		return $.trim(ret);
	}

	// Capitalize the first character of the string argument.
	function capitalize(sz) {
		return sz.substring ? sz.substring(0, 1).toUpperCase() + sz.substring(1, sz.length) : sz;
	}

	__private = {
		validate : validate,
		addToReport : addToReport,
		raiseError : raiseError,
		raiseAggregateError : raiseAggregateError,
		numericSum : numericSum,
		format : format,
		infer : infer,
		capitalize : capitalize
	};

})(jQuery);
// Output modes:
// //////////////////////////////////////////////////////////////

// Each output mode gets its own closure,
// distinct from the validity closure.

// Install the tooltip output.
(function($) {
	$.validity.outputs.tooltip = {
		tooltipClass : "validity-tooltip",

		start : function() {
			$("." + $.validity.outputs.tooltip.tooltipClass).remove();
		},

		end : function(results) {
			// If not valid and scrollTo is enabled, scroll the page to the first error.
			if (!results.valid && $.validity.settings.scrollTo) {
				$(document).scrollTop($("." + $.validity.outputs.tooltip.tooltipClass).offset().top);
			}
		},

		raise : function($obj, msg) {
			var pos = $obj.offset();
			pos.left += $obj.width() + 18;
			pos.top += 8;

			$("<div class=\"validity-tooltip\">" + msg + "<div class=\"validity-tooltip-outer\">" + "<div class=\"validity-tooltip-inner\"></div>" + "</div>" + "</div>").click(function() {
				$obj.focus();
				$(this).fadeOut();
			}).css(pos).hide().appendTo("body").fadeIn();
		},

		raiseAggregate : function($obj, msg) {
			// Just raise the error on the last input.
			if ($obj.length) {
				this.raise($obj.filter(":last"), msg);
			}
		}
	};
})(jQuery);

// Install the label output.
(function($) {
	function getIdentifier($obj) {
		return $obj.attr('id').length ? $obj.attr('id') : $obj.attr('name');
	}

	$.validity.outputs.label = {
		cssClass : "error",

		start : function() {
			// Remove all the existing error labels.
			$("." + $.validity.outputs.label.cssClass).remove();
		},

		end : function(results) {
			// If not valid and scrollTo is enabled, scroll the page to the first error.
			if (!results.valid && $.validity.settings.scrollTo) {
				location.hash = $("." + $.validity.outputs.label.cssClass + ":eq(0)").attr('for');
			}
		},

		raise : function($obj, msg) {
			var labelSelector = "." + $.validity.outputs.label.cssClass + "[for='" + getIdentifier($obj) + "']";

			// If an error label already exists for the bad input just update its text:
			if ($(labelSelector).length) {
				$(labelSelector).text(msg);
			}

			// Otherwize create a new one and stick it after the input:
			else {
				$("<label/>").attr("for", getIdentifier($obj)).addClass($.validity.outputs.label.cssClass).text(msg)

				// In the case that the element does not have an id
				// then the for attribute in the label will not cause
				// clicking the label to focus the element. This line
				// will make that happen.
				.click(function() {
					if ($obj.length) {
						$obj[0].select();
					}
				})

				.insertAfter($obj);
			}
		},

		raiseAggregate : function($obj, msg) {
			// Just raise the error on the last input.
			if ($obj.length) {
				this.raise($($obj.get($obj.length - 1)), msg);
			}
		}
	};
})(jQuery);

// Install the modal output.
(function($) {
	var
	// Class to apply to modal errors.
	errorClass = "validity-modal-msg",

	// The selector for the element where modal errors will me injected semantically.
	container = "body";

	$.validity.outputs.modal = {
		start : function() {
			// Remove all the existing errors.
			$("." + errorClass).remove();
		},

		end : function(results) {
			// If not valid and scrollTo is enabled, scroll the page to the first error.
			if (!results.valid && $.validity.settings.scrollTo) {
				location.hash = $("." + errorClass + ":eq(0)").attr('id');
			}
		},

		raise : function($obj, msg) {
			if ($obj.length) {
				var off = $obj.offset(), obj = $obj.get(0),

				// Design a style object based off of the input's location.
				errorStyle = {
					left : parseInt(off.left + $obj.width() + 4, 10) + "px",
					top : parseInt(off.top - 10, 10) + "px"
				};

				// Create one and position it next to the input.
				$("<div/>").addClass(errorClass).css(errorStyle).text(msg).click($.validity.settings.modalErrorsClickable ? function() {
					$(this).remove();
				} : null).appendTo(container);
			}
		},

		raiseAggregate : function($obj, msg) {
			// Just raise the error on the last input.
			if ($obj.length) {
				this.raise($($obj.get($obj.length - 1)), msg);
			}
		}
	};
})(jQuery);

// Install the summary output
(function($) {
	var
	// Container contains the summary. This is the element that is shown or hidden.
	container = ".validity-summary-container",

	// Erroneous refers to an input with an invalid value,
	// not the error message itself.
	erroneous = "validity-erroneous",

	// Selector for erroneous inputs.
	errors = "." + erroneous,

	// The wrapper for entries in the summary.
	wrapper = "<li/>",

	// Buffer to contain all the error messages that build up during validation.
	// When validation ends, it'll be flushed into the summary.
	// This way, the summary doesn't flicker empty then fill up.
	buffer = [];

	$.validity.outputs.summary = {
		start : function() {
			$(errors).removeClass(erroneous);
			buffer = [];
		},

		end : function(results) {
			// Hide the container and empty its summary.
			$(container).hide().find("ul").html('');

			// If there are any errors at all:
			// (Otherwise the container shouldn't be shown):
			if (buffer.length) {
				// Use integer based iteration for solution to Issue 7.
				for ( var i = 0; i < buffer.length; ++i) {
					$(wrapper).text(buffer[i]).appendTo(container + " ul");
				}

				$(container).show();

				// If scrollTo is enabled, scroll the page to the first error.
				if ($.validity.settings.scrollTo) {
					location.hash = $(errors + ":eq(0)").attr("id");
				}
			}
		},

		raise : function($obj, msg) {
			buffer.push(msg);
			$obj.addClass(erroneous);
		},

		raiseAggregate : function($obj, msg) {
			this.raise($obj, msg);
		},

		container : function() {
			document.write("<div class=\"validity-summary-container\">" + "The form didn't submit for the following reason(s):" + "<ul></ul>" + "</div>");
		}
	};
})(jQuery);
function byteLength(str) {
	var byteLen = 0, len = str.length;
	if (!str)
		return 0;
	for (var i = 0; i < len; i++)
		byteLen += str.charCodeAt(i) > 255 ? 1:1;
	return byteLen;
}