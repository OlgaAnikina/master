var messageApi = Vue.resource('/v1/messages{/id}');

Vue.component('message-form', {
    props:['messages'],
    data: function() {
        return {
            text: '',

        }
    },

    template: '<div >'+
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
    template: '<div class="block1"><div>{{message.text}}</div>' +
        '<div>{{message.authorName}}</div>' +
        '</div>'
});

Vue.component('messages-list', {
    props: ['messages'],
    template: '<div class="tableChat ">' +
        '<message-form :messages="messages" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message"/>' +
        '</div>',


});

Vue.component('user-row', {
    props: ['user'],
    template: '<div>{{ user.name}}</div>'
});

Vue.component('users-list', {
    props: ['users'],
    template:
        '<div>' +
        '<h3>You can choose one of users: </h3>' +
        '<user-row v-for="user in users"  :user="user"/>' +
        '</div>'
});

var app = new Vue({
    el: '#app',
    template: '<div>' +

        '<div v-if="!profile">You can authorize <a href="/login">here</a> </div>' +
        '<div v-if="!profile">or add new profile <a href="/registration">here</a> </div>' +
        '<div v-else> <div>{{profile.name}}&nbsp;<a href="/logout">Log out</a>' +
     //   '<div><users-list :users="users" /> </div>' +
        '</div></div>' +
        '<messages-list :messages="messages"/>' +

        '</div>',
    data: {
     //   users: frontendData.users,
        messages: frontendData.messages,
        profile: frontendData.profile
    },
    created: function () {
 
    }
});