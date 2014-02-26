jQuery(document).ready(function(){
	new OlympicRouter();
});

var OlympicRouter = Backbone.Router.extend({
	initialize: function(){
		this.getArchetypes();
	},
	getArchetypes: function(){
		jQuery.ajax({url: "inspectionArchetype.jsp"}).
		done(jQuery.proxy(this.renderMenu, this)).
		fail(function(err){console.log(err);});
	},
	aArchetypes: [],
	fMenuTemplate: _.template(jQuery("#navTemplate").html()),
	bInitialized: false,
	renderMenu: function(aArchetypes){
		this.aArchetypes = aArchetypes; 
		for(var n = 0; n < aArchetypes.length; n++){
			jQuery("#navPlaceholder").prepend(this.fMenuTemplate(aArchetypes[n]));
		}
		if(this.bInitialized)
			jQuery("#results").html("archetype inserted");
		else{
			Backbone.history.start();
			this.bInitialized = true;
		}
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
		jQuery("#addArchetype").submit(jQuery.proxy(this.addArchetype, this));
	},
	addAttribute: function(){
		jQuery("#formPlaceholder").append(this.fAddAttributeTemplate());
		return false;
	},
	addArchetype: function(){
		var oNew = {};
		oNew.name = jQuery("#archetype").val();
		oNew.archetypeAttributes = [];
		var aAttributes = jQuery("#formPlaceholder").find("tr");
		for(var n = 0; n < aAttributes.length; n++){
			var oAttribute = {};
			oAttribute.name = jQuery(jQuery(aAttributes[n]).find("input")[0]).val();
			oAttribute.formType = jQuery(jQuery(aAttributes[n]).find("select")[0]).val();
			switch(oAttribute.formType){
				case 'number':
					oAttribute.SQLType = 'INT';
					break;
				case 'date':
					oAttribute.SQLType = 'DATE';
					break;
				default:
					oAttribute.SQLType = 'VARCHAR(45)';
					break;
					
			}
			oNew.archetypeAttributes.push(oAttribute);
		}
		jQuery.ajax({
			url: "inspectionArchetype.jsp",
			data: JSON.stringify(oNew),
			dataType:"json",
			type: "post"
		}).
		done(jQuery.proxy(this.getArchetypes, this)).
		fail(function(err){
			jQuery("#results").html("error: " + err.responseText);
			console.log(err);
		});
		return false;
	},
	sCurrentArchetype: "",
	showArchetype: function(sArchetype){
		if(this.aArchetypes.length == 0)return;
		if(sArchetype == null){
			sArchetype = this.aArchetypes[this.aArchetypes.length - 1].NAME;
		}
		if(this.sCurrentArchetype != sArchetype){
			this.sCurrentArchetype = sArchetype;
			this.getObjects();
		}
	},
	getObjects: function()
	{
		jQuery.ajax({
			url:"inspectionObject.jsp?archetype=" + this.sCurrentArchetype,
			dataType:"json"
		}).
		done(jQuery.proxy(this.showObjects, this)).
		fail(function(err){console.log(err);});	
	},
	fObjectTemplate: _.template(jQuery("#objectTemplate").html()),
	showObjects: function(aObjects){
		jQuery("#mainPlaceholder").html(this.fObjectTemplate({archetype: this.sCurrentArchetype}));
		jQuery("#objectForm").submit(jQuery.proxy(this.addObject, this));
		
		// go get the fields for the form
		jQuery.ajax({
			url:"inspectionArchetype.jsp?archetype=" + this.sCurrentArchetype,
			dataType:"json"
		}).
		done(jQuery.proxy(this.showFields, this)).
		fail(function(err){console.log(err);});	
		for(var n = 0; n < aObjects.length; n++){
			if(n == 0){
				// render headings
				var tr = jQuery('<tr></tr>');
				jQuery.each(aObjects[n], function(skey2, sValue2){
					jQuery('<th>'+ skey2 +'</th>').appendTo(tr);
				});
				jQuery("<thead id=\"heading\"></thead>").appendTo('#objectResults');
				tr.appendTo('#heading');
			}
			//render row
			var tr = jQuery('<tr></tr>');
			jQuery.each(aObjects[n], function(skey2, sValue2){
				jQuery('<td>'+ sValue2 +'</td>').appendTo(tr);
			});
			tr.appendTo('#objectResults');

		}
	},
	aFields: [],
	fFieldsTemplate: _.template(jQuery("#fieldTemplate").html()),
	showFields: function(aFields){
		this.aFields = aFields;
		for(var n = 0; n < aFields.length; n++){
			var oField = aFields[n];
			oField.id = n;
			jQuery("#fields").append(this.fFieldsTemplate(oField));
		}
		jQuery("#field0").focus();
	},
	addObject: function()
	{
		var oNew = {archetype: this.sCurrentArchetype};
		var aFields = jQuery("#fields").find("input");
		for(var n = 0; n < aFields.length; n++){
			oNew[jQuery(aFields[n]).attr("name")] = jQuery(aFields[n]).val(); 
		}
		jQuery.ajax({
			url: "inspectionObject.jsp",
			data: JSON.stringify(oNew),
			dataType:"json",
			type: "post"
		}).
		done(jQuery.proxy(this.getObjects, this)).
		fail(function(err){
			jQuery("#results").html("error: " + err.responseText);
			console.log(err);
		});
		return false;
	}
});