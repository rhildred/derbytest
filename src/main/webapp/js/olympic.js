jQuery(document).ready(function(){
	new OlympicRouter();
});

var OlympicRouter = Backbone.Router.extend({
	initialize: function(){
		jQuery.ajax({url: "inspectionArchetype.jsp"}).
		done(jQuery.proxy(this.renderMenu, this)).
		fail(function(err){console.log(err);});
	},
	aArchetypes: [],
	fMenuTemplate: _.template(jQuery("#navTemplate").html()),
	renderMenu: function(aArchetypes){
		this.aArchetypes = aArchetypes; 
		for(var n = 0; n < aArchetypes.length; n++){
			jQuery("#navPlaceholder").prepend(this.fMenuTemplate(aArchetypes[n]));
		}
		Backbone.history.start();
	},
	routes:{
		"addNew": "addNew",
		"(:sArchetype)": "showArchetype"
	},
	fAddNewTemplate: _.template(jQuery("#addNewTemplate").html()),
	fAddAttributeTemplate: _.template(jQuery("#addAttributeTemplate").html()),
	addNew: function(){
		jQuery("#mainPlaceholder").html(this.fAddNewTemplate());
		jQuery("#formPlaceholder").append(this.fAddAttributeTemplate());
		jQuery("#addAttribute").click(jQuery.proxy(this.addAttribute, this));
	},
	addAttribute: function(){
		jQuery("#formPlaceholder").append(this.fAddAttributeTemplate());
		return false;
	},
	sCurrentArchetype: "",
	showArchetype: function(sArchetype){
		if(this.aArchetypes.length == 0)return;
		if(typeof sArchetype == "undefined"){
			sArchetype = this.aArchetypes[this.aArchetypes.length - 1].NAME;
		}
		this.sCurrentArchetype = sArchetype;
		alert(sArchetype + " is the current");
	}
});