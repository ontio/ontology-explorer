<template>
  <div class="container">
    <div class="div-address-msg form-group">
      <p class="title">{{ $t('addressMsg.name') }}</p>

      <div class="row"><div class="col-lg-12"><hr class="address-msg-hr"></div></div>

      <div class="row justify-content-center">
        <div class="col-lg-12">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="tab-border-top-none" scope="col">{{ $t('addressMsg.address') }}</th>
              <th class="tab-border-top-none" scope="col">{{ $t('addressMsg.latestDealTime') }}</th>
              <th class="tab-border-top-none" scope="col">{{ $t('addressMsg.dealNumber') }}</th>
              <th class="tab-border-top-none" scope="col">{{ $t('addressMsg.addressGenerationTime') }}</th>
              <th class="tab-border-top-none" scope="col">{{ $t('addressMsg.balance') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>Column content</td>
              <td>Column content</td>
              <td>Column content</td>
              <td>Column content</td>
              <td>Column content</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "address-msg"
    }
</script>

<style scoped>
  .div-address-msg {
    height: 360px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-radius: 0.25rem;
    padding: 15px;
  }
  .address-msg-hr {
    height: 1px;
  }
  .tab-border-top-none{
    border-top: none;
  }
</style>
