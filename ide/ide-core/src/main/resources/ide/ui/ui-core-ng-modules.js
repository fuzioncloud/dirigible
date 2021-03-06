/**
 * Provides key microservices for constructing and managing the IDE UI
 *
*/
angular.module('ideUiCore', ['ngResource'])
.provider('messageHub', function MessageHubProvider() {
  this.evtNamePrefix = '';
  this.evtNameDelimiter = '.';
  this.$get = [function messageHubFactory() {
    var messageHub = new FramesMessageHub();
	//normalize prefix if any
	this.evtNamePrefix = this.evtNamePrefix || '';
	this.evtNamePrefix = this.evtNamePrefix ? (this.evtNamePrefix+this.evtNameDelimiter): this.evtNamePrefix;
	var message = function(evtName, data){
		if(!evtName)
			throw Error('evtname argument must be a valid string, identifying an existing event');
		messageHub.post({data: data}, this.evtNamePrefix + evtName);
	}.bind(this);
	var on = function(evtName, callbackFunc){
		if(typeof callbackFunc !== 'function')
			throw Error('Callback argument must be a function');
		messageHub.subscribe(callbackFunc, evtName);
	};
	return {
		message: message,
		on: on
	};
  }];
})
.factory('Theme', ['$resource', function($resource){
	var themeswitcher = $resource('/services/v3/core/theme?name=:themeName', {themeName: 'default'});
	var themes = {
		"default": "/services/v3/web/resources/themes/default/bootstrap.min.css",
		"wendy" : "/services/v3/web/resources/themes/wendy/bootstrap.min.css",
		"baroness" : "/services/v3/web/resources/themes/baroness/bootstrap.min.css"
	};
	return {
		changeTheme: function(themeName){
			return themeswitcher.get({'themeName':themeName});
		},
		themeUrl: function(themeName){
			return themes[themeName];
		},
		reload: function(){
			location.reload();
		}
	}
}])
.service('Perspectives', ['$resource', function($resource){
	return $resource('../../js/ide/services/perspectives.js');
}])
.service('Menu', ['$resource', function($resource){
	return $resource('../../js/ide/services/menu.js');
}])
.service('User', ['$http', function($http){
	return {
		get: function(){
			var user = {};
			$http({
				url: '../../js/ide/services/user-name.js',
				method: 'GET'
			}).success(function(data){
				user.name = data;
			});
			return user;
		}
	};
}])
/**
 * Creates a map object associating a view factory function with a name (id)
 */
.provider('ViewFactories', function(){
	this.factories = {
			"frame": function(container, componentState){
				container.setTitle(componentState.label || 'View');
					$('<iframe>').attr('src', componentState.path).appendTo(container.getElement().empty());
			},
			"editor": function(container, componentState){
				this.setContent = function(path){
					var src;
					if (path) {
						src = '../ide-orion/editor.html?file='+path;
					} else {
						container.setTitle("Welcome");
						src = '/services/v3/web/ide/welcome.html';
					}
					$('<iframe>').attr('src', src).appendTo(container.getElement().empty());
				};
				this.setContent(componentState.path);
			}
	};
	this.$get = [function viewFactoriesFactory() {
		return this.factories;
	}];
})
/**
 * Wrap the ViewRegistry class in an angular service object for dependency injection
 */
.service('ViewRegistrySvc', ViewRegistry)
/**
 * A view registry instance factory, using remote service for intializing the view definitions
 */
.factory('viewRegistry', ['ViewRegistrySvc', '$resource', 'ViewFactories', function(ViewRegistrySvc, $resource, ViewFactories){
	Object.keys(ViewFactories).forEach(function(factoryName){
		ViewRegistrySvc.factory(factoryName, ViewFactories[factoryName]);
	});		
	var get = function(){
		return $resource('../../js/ide/services/views.js').query().$promise
				.then(function(data){
					data = data.map(function(v){
						v.id = v.id || v.name.toLowerCase();
						v.label = v.label || v.name;
						v.factory = v.factory || 'frame';
						v.settings = {
							"path": v.link
						}
						v.region = v.region || 'left-top';
						return v;
					});
					//no extension point. provisioned "manually"
					data.push({ "id": "editor", "factory": "editor", "region": "center-middle", "label":"Editor", "settings": {}});
					//no extension point yet
					data.push({ "id": "result", "factory": "frame", "region": "center-bottom", "label":"Result", "settings": {"path":  "../ide-database/sql/result.html"}});
					data.push({ "id": "properties", "factory": "frame", "region": "center-bottom", "label":"Properties", "settings": {"path":  "../ide/properties.html"}});
					data.push({ "id": "sql", "factory": "frame", "region": "center-middle", "label":"SQL", "settings": {"path":  "../ide-database/sql/editor.html"}});
					//register views
					data.forEach(function(viewDef){
						ViewRegistrySvc.view(viewDef.id, viewDef.factory, viewDef.region, viewDef.label,  viewDef.settings);
					});
					return ViewRegistrySvc;
				});
	};
	
	return {
		get: get
	};
}])
.factory('Layouts', [function(){
	return {
		manager: undefined
	};
}])
.directive('menu', ['$resource', 'Theme', 'User', 'Layouts', function($resource, Theme, User, Layouts){
	return {
		restrict: 'AE',
		transclude: true,
		replace: 'true',
		scope: {
			url: '@menuDataUrl',
			menu:  '=menuData'
		},
		link: function(scope, el, attrs){
			var url = scope.url;
			function loadMenu(){
				scope.menu = $resource(url).query();
			}
			if(!scope.menu && url)
				loadMenu.call(scope);
			scope.menuClick = function(item, subItem) {
				if(item.name === 'Show View'){
					// open view
					Layouts.manager.openView(subItem.name.toLowerCase());
				} else if(item.name === 'Open Perspective'){
					// open perspective`
					window.open(subItem.onClick.substring(subItem.onClick.indexOf('(')+2, subItem.onClick.indexOf(',')-1));//TODO: change the menu service ot provide paths instead
				}						
			};
			scope.selectTheme = function(themeName){
				Theme.changeTheme(themeName);
				var themeUrl = Theme.themeUrl(themeName);
				Theme.reload();
			};
			scope.user = User.get();
		},
		templateUrl: '/services/v3/web/ide/ui/tmpl/menu.html'
	}
}])
.directive('sidebar', ['Perspectives', function(Perspectives){
	return {
		restrict: 'AE',
		transclude: true,
		replace: 'true',
		scope: {
			active: '@'
		},
		link: function(scope, el, attrs){
			scope.perspectives = Perspectives.query();
		},
		templateUrl: '/services/v3/web/ide/ui/tmpl/sidebar.html'
	}
}])
.directive('statusBar', ['messageHub', function(messageHub){
	return {
		restrict: 'AE',
		scope: {
			statusBarTopic: '@'
		},
		link: function(scope, el, attrs){
			messageHub.on(scope.statusBarTopic || 'status.message', function(msg){
				scope.message = msg.data;
			});
		}
	}
}])
.directive('viewsLayout', ['viewRegistry', 'Layouts', function(viewRegistry, Layouts){
	return {
		restrict: 'AE',
		scope: {
			viewsLayoutModel: '=',
			viewsLayoutViews: '@',
		},
		link: function(scope, el, attrs){
			var views;
			if(scope.layoutViews){
				views = scope.layoutViews.split(',');
			} else {
				views =  scope.viewsLayoutModel.views;
			}
			var eventHandlers = scope.viewsLayoutModel.events;
			
			viewRegistry.get().then(function(registry){
				scope.layoutManager = new LayoutController(registry);
				if(eventHandlers){
					Object.keys(eventHandlers).forEach(function(evtName){
						var handler = eventHandlers[evtName];
						if(typeof handler === 'function')
							scope.layoutManager.addListener(evtName, handler);
					});
				}
				$(window).resize(function(){scope.layoutManager.layout.updateSize()});
				scope.layoutManager.init(el, views);
				Layouts.manager = scope.layoutManager;
			});
		}
	}
}])	;