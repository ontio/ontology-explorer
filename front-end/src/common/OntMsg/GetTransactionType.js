/**
 * The lang saved to the localStorage.
 */
export default {
  getTransactionType($case) {
    switch ($case) {
      case 1:
        return "发行"
      case 64:
        return "注册资产"
      case 128:
        return "转账"
      case 129:
        return "存证"
      case 208:
        return "智能合约"
      case 209:
        return "智能合约"
    }
  }
}
