
<div class="row-fluid">
    <div class="span12 label-" >
        <div class="span6 text-left">
            <label>Survey status: <span data-bind="attr:{'class': published() ? 'badge badge-success' : 'badge badge-important'}, text: published() ? 'Published' : 'Unpublished'"></span></label>
        </div>
        <div class="span6 text-right">
            <button data-bind="attr:{'class': published() ? 'btn btn-primary' : 'btn btn-success'}, click: $root.publish, text: published() ? 'Unpublish' : 'Publish' "></button>
        </div>
    </div>

</div>

