var messageApi = Vue.resource('/v1/messages{/id}');

Vue.component('message-form', {
    props:['messages'],
    data: function() {
        return {
            text: ''
        }
    },

    template: '<div>'+
        '<input type="text" placeholder="Text of message" v-model="text"/>' +
        '<input type="button" :class="{button:true}" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function () {
            var message = {text: this.text};
            messageApi.save({}, message).then(result =>
                result.json().then(data =>{
                    this.messages.push(data);
                })
            )
        }
    }
});

Vue.component('message-row', {
    props: ['message'],
    template: '<tr><th>{{message.text}}</th></tr>'
});

Vue.component('messages-list', {
    props: ['messages'],
    template: '<table>' +
        '<message-form :messages="messages" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message"/>' +
        '</table>',

    created: function () {
        messageApi.get().then(result =>
        result.json().then(data=>
        data.forEach(message => this.messages.push(message))))

    }
});


var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages"/>',
    data: {
        messages: []
    }
});