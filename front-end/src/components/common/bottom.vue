<template>
  <div id="bottom">
    <footer class="container-val-background">
        <div class="container footer-container" style="padding:0px">
            <div class="container-margin-footer">
                <div class="row copyright" style="color:grey">
                    <div class="col-sm-12 footer-mail-margin" style="padding:0px"> 
                        <!-- <img @click="toHome()" style="width:219px;" src="../assets/images/footerlogo.png" /> -->
                        <p class="margin4-top font-size14 f_color font-Regular bottom_p " >Copyright © 2018 The Ontology Team</p>
                    </div>
                </div>
            </div>
        </div>

    </footer>
  </div>
</template>

<script>
  export default {
    name: 'bottom',
    beforeDestroy() {
    }
  }
</script>

<style>
 .logo{
   width: 100px;
 }
 .bottom-content{
   /*position: fixed;
   bottom: 0;*/
   width: 100%;
   height: 50px;
   text-align: center;
   padding-top: 10px;
   padding-bottom: 10px;
   color: gray;
   /*background-color: #25262C;*/
 }
 #bottom{
   position: relative;
   width: 100%;
   bottom: 0;
 }
 .bottom_p{
   text-align:center;
   padding:48px 0;
 }
</style>