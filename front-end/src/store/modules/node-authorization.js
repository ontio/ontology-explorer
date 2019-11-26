import axios from 'axios'
import * as types from "../mutation-type"
import numeral from 'numeral'

/**
 * 写死的节点数据。。。
 *
 * @return {*[]}
 */
function nodeDetailData() {
  return [
    {
      "nodename": "Trio Bravo",
      "publickey": "0287fe995c6f27ad0c1a7640f9cc6c2537ed47126cc430e738d96f0390583ac2d2",
      "address": "AQE2zwXxhUV1BX6arPcv2oD4AgpWQfGGdM",
      "ontid": "did:ont:ARv2DJEhnAwmXmqfkNzHK3wFMJPznSR7mv",
    },
    {
      "nodename": "Dubhe",
      "publickey": "02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7",
      "address": "AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1",
      "ontid": "did:ont:AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1",
    },
    {
      "nodename": "Merak",
      "publickey": "0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4",
      "address": "AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci",
      "ontid": "did:ont:AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci",
    },
    {
      "nodename": "Phecda",
      "publickey": "022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0",
      "address": "AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D",
      "ontid": "did:ont:AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D",
    },
    {
      "nodename": "Megrez",
      "publickey": "0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa",
      "address": "AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq",
      "ontid": "did:ont:AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq",
    },
    {
      "nodename": "Alioth",
      "publickey": "022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb",
      "address": "APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ",
      "ontid": "did:ont:APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ",
    },
    {
      "nodename": "Mixar",
      "publickey": "02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444",
      "address": "AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU",
      "ontid": "did:ont:AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU",
    },
    {
      "nodename": "Alkaid",
      "publickey": "03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53",
      "address": "AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4",
      "ontid": "did:ont:AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4",
    },
    {
      "nodename": "Jumple Foundation",
      "publickey": "038602b1b03637be0de5305639756db44da31b5241acde4cff86f2a181f76d9fee",
      "address": "AYgiXzs4b7XmaQjNoo6ANuFJ5zHDebgPdq",
      "ontid": "did:ont:AdwiepZeuRuz7QsJGhzYhPWDpcGSrQdrjb",
    },
    {
      "nodename": "Hashed",
      "publickey": "03ee9fd0557e56dab1eb7d9720e749d581fb0ebfd0a15f31bc973834c36e0b9c46",
      "address": "AVaQJM27YxLkD5JAd1n4wGnxMx2Ey1h9cQ",
      "ontid": "did:ont:AJc7haixsxxCKAAVSoJbFLk4RhkhgBvBQZ",
      "ip": "52.198.20.122",
      "Intro": "Hashed is blockchain accelerator and community builder based out in South Korea and San Francisco. The firm was founded by serial entrepreneurs, company operators, and systems engineers; passionate about enabling the global adoption of blockchain through education, acceleration, and impact investments. "
    },
    {
      "nodename": "Hashed operates Hashed Lounge; a premier blockchain meetup group. Hashed Post; a cryptocurrency blog interviewing founders and thought leaders. And Hashed Night; a world-class conference, events, and networking brand. The team is actively working towards defining what it means to be a fundamental technology and founder focused investment startup in the blockchain ecosystem.",
      "publickey": "Hashed Facebook: https://www.facebook.com/hashedfund/"
    },
    {
      "nodename": "Hashed LinkedIn: https://www.linkedin.com/company/hashedfund/",
      "publickey": "1. Informed and promoted Ontology project to Korean developers - We have already conducted 2 offline meetings through Hashed Lounge and have been working on Ontology project promotion through Hashed Post interview."
    },
    {
      "nodename": "Infinity Stones",
      "publickey": "0244c959ed3bcd0f96da35d4c615bcf99c8a2d7ddb875142fa8f731bbf681eee44",
      "address": "AbZuVX9M2F4cw6myDFVP9shAFKPm8xBY9J",
      "ontid": "did:ont:AWFR1rPJZXVSrpVksg239F4bSN1mw2z11E",
      "ip": "34.208.219.225",
      "Intro": "Infinity Stones is a Silicon Valley-based technology company that specializes in both cloud management platforms and cyber security protections for blockchain. Our team is comprised of PhDs and technology leaders from established companies in the hi-tech industries such as Google, Oracle, and Intel.",
      "Website": "http://www.infinitystones.io/"
    },
    {
      "nodename": "Twitter: https://twitter.com/infstones",
      "publickey": "Infinity Stones is willing to build long-term partnership with Ontology in the following areas:"
    },
    {
      "nodename": "Martview",
      "publickey": "03a0cf8494ef9901c7a78df98abb9a8a245961da77c157cb3f31e67db0325f070c",
      "address": "AUEAGG1pWTg2nAMsoR4x6EvSN2wb2wdZHx",
      "ontid": "did:ont:ASCx8f3Dv6fuqcKXYvUmbyHbqo38anTzT1",
      "ip": "13.238.191.180",
      "Intro": "Martview was founded in 2005 and mainly focuses at the GSM telecommunication and electronics gadget industry. The company also maintains a full online business operation which includes flashing and unlocking devices, and a wide range of electronic gadgets spare parts and accessories. Martview is also an ebook reader application developer.",
      "Website": "martview.com"
    },
    {
      "nodename": "Facebook: Facebook.com/martview",
      "publickey": "Martview is planning to integrate real world business with the decentralized blockchain marketplace for flashing and unlocking devices, powered by Ontology ecosystem. The company believes the Ontology blockchain is the first choice because of its advanced platform that goes beyond to provide the necessary to cater and automate huge company processes and transactions that to solve severe company issues and the possibility of smart packaging and intelligent inventory management."
    },
    {
      "nodename": "DAD",
      "publickey": "02872b863b9bee7a1bd15692330af591603bc3db3444272c00e51997a74ac1756c",
      "address": "AbPsjYENUywQDDNdMq8iCGHXLduP1zbqaZ",
      "ontid": "did:ont:Ad8MmnWgZWHsi7mJEcuNbWycKEbDXE2a7Q",
      "ip": "35.185.228.58",
      "Intro": "The DAD Foundation is dedicated to bringing openness, transparency, and efficiency to online advertising."
    },
    {
      "nodename": "Built on Ontology's distributed trust collaboration platform, DAD achieves data openness, transaction transparency, and revenue sharing with users. With improved ad quality and delivery efficiency, DAD aims at building a new generation of a blockchain ad system and reshaping the ad industry as we know it today.",
      "publickey": "https://dad.one/"
    },
    {
      "nodename": "YouTube: https://www.youtube.com/channel/UCQKKAbpbB3ABrITnlqEn5qA",
      "publickey": "Ontology will enable trust collaboration and data exchanging on a large scale. Data competitiveness encourages business parties to hold data privately and only share minimally when necessary. With the trust framework and data exchanging system provided by Ontology, it will for the first time in history to be technically feasible for the industry to embrace large scale data sharing."
    },
    {
      "nodename": "SunStar",
      "publickey": "02584b9aa8d871017ff7d19fb8c7c934eeffbdc6b7de5501855e11c65ffbb67774",
      "address": "AP1UdrXhqnxoBUxmpvguuXMEXB8eR5cHkc",
      "ontid": "did:ont:AHUZodjwH25kn9mLGBJZuFMVZRzTfu7fjb",
    },
    {
      "nodename": "G&Q TECH LIMITED",
      "publickey": "036f29e06e57f36432ad6bb89dbfd8f81d12e42e7a1a5d454c322ac8e3b94a4bf0",
      "address": "AX9MxQSbQPKKA4cP9VzTwE8o6MXC3pC9Nw",
      "ontid": "did:ont:Adqvs65BWKURFZEnrqyXkaexvbYB2XhXwp",
      "ip": "18.179.249.27",
      "Intro": "G&Q TECH LIMITED is located in South Africa and committed to the development of blockchain technology. It’s pleased to cooperate with Ontology to develop blockchain.",
      "Vision": "We are a group of technology enthusiasts with strong technical skills and can continue to contribute to the development of the community."
    },
    {
      "nodename": "InWeCrypto",
      "publickey": "026dd0e7b95843181e0f986ecb292f32a2165261ca62d097bba8c54195d527f73f",
      "address": "AdZFF1ottgfqNut5R64KZXsh3p6PMULfZD",
      "ontid": "did:ont:Ac5fQiSvKNYbM7bPpYvvNvwhFz88MyDFMK",
      "ip": "47.254.149.153",
      "Intro": "InWeCrypto is a blockchain media and blockchain assets management organization.",
      "Website": "http://inwecrypto.com"
    },
    {
      "nodename": "Medium: https://medium.com/@inwecrypto",
      "publickey": "InWeCrypto expects Ontology to implement landing projects in more fields."
    },
    {
      "nodename": "gf.network",
      "publickey": "031554b64b4145fb33e83f686081e9148188c2176aaf6a55c874b38fd093e1bc0c",
      "address": "AGUekTnhucrQShdATUFhZqFqiPdC65nRxv",
      "ontid": "did:ont:ATYM4p3hQAKB9ekzNWUVYD7Qb38hCkroj8",
    },
    {
      "nodename": "J&D TECH LIMITED",
      "publickey": "033ad721e2443998bc036710b0415ac6ae67e2d490307ba9f4f8f41af62726f34d",
      "address": "ARXKEj5r61cWm1X7DLZttDJo3D5Zhwdexc",
      "ontid": "did:ont:AG5Bk9YV74qEtt7u5dGYpuLcwTbWSjz6Tg",
      "ip": "52.194.87.222",
      "Intro": "J&D TECH LIMITED is committed to the ecosystem development of blockchain technology. It also cooperates with many blockchain organizations, such as the Huobi Research Institute.",
      "Vision": "We will try our best to contribute to the Ontology community."
    },
    {
      "nodename": "ont-huobipool",
      "publickey": "03c793b05687137803952cb0327aaed8cfb1a7199badd23a34b2fa8417f614faae",
      "address": "AY65tbb1bzDJ4fbcqPCExMyMEq2BRNb9fu",
      "ontid": "did:ont:AUXCJs76ijZU38sePH92SnUaod7P5tUmEx",
      "ip": "52.69.99.117",
      "Intro": "Huobipool has powerful technical research and development strength and can provide more use cases for the Ontology ecosystem by developing dApps.",
      "Website": "https://www.huobipool.com/#!/community"
    },
    {
      "nodename": "Facebook: https://www.facebook.com/huobipool",
      "publickey": "Huobipool is committed to enabling more developers to understand and participate in the ecosystem development of Ontology."
    },
    {
      "nodename": "Accomplice",
      "publickey": "034a7069c3ba649a762191b62e29992f6b4ba4b670d6502259e15597199cea17d2",
      "address": "AGgP7kWDSxzbRHdeDwULwYUV7qVuKWBoTr",
      "ontid": "did:ont:Ae52qMcDtJ7z2grGKFw89TCia8G7tjBeCZ",
      "ip": "13.232.196.82",
      "Intro": "Accomplice is the premier decentralized venture capital firm in the United States. While not an incubator program like Y-Combinator or TechStars, Accomplice pioneered the first-of-its kind platform for decentralized VC tech investing at scale: Angel List - www.Angel.co. Angel List has grown to tens of thousands of global investors who syndicate and or directly invest $100’s of millions in thousands of startups. "
    },
    {"nodename": "• 协议实验室:FileCoin 和 IPFS 项目", "publickey": "https://accomplice.co/"},
    {
      "nodename": "Twitter: https://twitter.com/Accomplices",
      "publickey": "Accomplice has a large and growing portfolio of projects both on-chain and off-chain that require robust performant identity-centric applications. We are doing – not planning – on working on various implementations with Ontology across the portfolio. This will help drive US-China blockchain permissioned and permissionless transactions in a new global smart economy."
    },
    {
      "nodename": "Matrix Partners China",
      "publickey": "035e470667371300f87090c3410cfbb24a4b40c6955574ed7dd053644f94922632",
      "address": "AKkcxjHGXnF68FYNP5UQ5Hkv4j3HBsdmP5",
      "ontid": "did:ont:AbYsc5Do8tG5eUyhhX4YAW9pqSYHHCBvox",
      "ip": "52.172.133.71",
      "Intro": "Matrix Partners China is committed to building long-term relationships with outstanding entrepreneurs and helping them build significant, industry-leading companies."
    },
    {
      "nodename": "Affiliated with Matrix Partners, a premier U.S. venture capital firm, Matrix Partners China was founded in 2008 to focus exclusively on investments in China.",
      "publickey": "https://www.matrixpartners.com.cn/index.php/en/",
      "address": "Ontology’s goal is to build “digital ID” on blockchain and create a trust collaboration platform. As a VC with various portfolios, we’ll introduce them to the ecosystem and use Ontology’s framework to build applications. we have already introduced some portfolios to the Ontology team to see if they are suitable for the ecosystem."
    },
    {
      "nodename": "CloudDesk",
      "publickey": "0349a0e3e018d275a09bb6d4237cb95ee199cf38c81c9a1228ecb72f2c6ab8fdfe",
      "address": "AXtswyDXUgkpUobpyc9cj8cTTAtAdMbTTy",
      "ontid": "did:ont:AVXVjmuFs9CoiHUiH4RCLyWwGByCag5SSN",
      "ip": "103.25.200.200",
      "Intro": "A blockchain digital platform enabler that is part of a publically listed company, Cloudaron Group Berhad (KLSE:03001).",
      "Website": "www.clouddesk.io",
      "Vision": "Increasing enterprise adoption on Ontology and blockchain-based technology in alignment with our existing customer base covering banking, government, and large enterprise."
    },
    {
      "nodename": "Collider",
      "publickey": "02da86115d1c2450eb5780da3ffd21c0e68c9094f990f3c7c489e0c5d50b72ec43",
      "address": "AZ3P2wyaHzbjQAqgFSDpaTavn2iUSFaHqi",
      "ontid": "did:ont:AWtA9kUr4wXc51WNmYwxd9GrTdyNrY5XMC",
      "ip": "178.128.252.38",
    },
    {
      "nodename": "CertiK",
      "publickey": "02b9000043dba3a3c91185a963babbb975daf73ebfc4f07ab7cb80dc7172145c3d",
      "address": "AGns9etVHUknEgZ6yUhnHSZm6G6AxKXkPx",
      "ontid": "did:ont:AdzY1ZW3HpwhMp2K3CxjkdgQSgg5Zwtm4Z",
      "ip": "52.63.130.88",
      "Intro": "CertiK is a research-driven company founded by Prof. Gu from Columbia University and Prof. Shao from Yale University. Our mission at CertiK is to give people the power to trust by providing the best formal verification platform for building fully trustworthy smart contracts and blockchain ecosystems.",
      "Website": "https://certik.org/"
    },
    {
      "nodename": "Twitter: https://twitter.com/certikorg",
      "publickey": "By utilizing our patented DeepSpec and formal verification techniques, CertiK plans to collaborate closely with Ontology to provide a high scalable specially designed formal verification engine to guard the safety of the Ontology ecosystem. We aim to help Ontology to build a fully trustworthy high-performance distributed trust collaboration platform and ecosystem."
    },
    {
      "nodename": "Avocado",
      "publickey": "034b87ac6f457e44a33284fcd60d2b2f6ea2a508e553462bba5bf526a0dd285300",
      "address": "Ab5H6QZLvpSZR24Zkuri8zQcJxswJetyxx",
      "ontid": "did:ont:AZ98Zmgt3nGNnMQF4hYmYAr75g6puJjZDd",
    },
    {
      "nodename": "Ontology Universe",
      "publickey": "02a0ca41d2544cdea25fe98102157ea9ca051ac1e565e43847c3f006c9caedc297",
      "address": "ARydHAisjz3BZhsU2kPbRdPrpq5Hb4GCj6",
      "ontid": "did:ont:AK1jMtS1AC1BvBptgUoFWPMhgEBh5ewyFm",
      "Intro": "Ontology Universe is a global Ontology fans club with a team of blockchain developers. We have lonf experience in running cloud-based production systems . Now we are global and open to all Ontology fans and end users. We welcome you to join us!",
      "Vision": "1. Offer potential cooperation opportunities with industry companies."
    },
    {
      "nodename": "ONTLabo",
      "publickey": "037acf1bb88200138b44f5d645d0e0c063043addc30db0102eea508db2585f9171",
      "address": "AHnRpJ8Hnk9eAdDsW1gMAwTK88pvtZEGfg",
      "ontid": "did:ont:AJXDuPpU41R96DV4FDuDqG7qCLWbVbr9Mu",
      "ip": "18.179.234.8",
      "Intro": "ONTLabo is a Tokyo-based Japanese Ontology fan club.",
      "Vision": "In the Japanese region, Ontology will be promoted through the community, the website, and Japanese language materials. W will also gradually increase the influence of Ontology in the Japanese region, and develop and expand Ontology to investors and enthusiasts."
    },
    {
      "nodename": "Blockchain Global Limited",
      "publickey": "03e96d1b80bfa4895763d640e6278c3e31323ba395cb32c9c578d54374e092e134",
      "address": "ARnpd59csGzfXmMhDCcmShE7DZqeoMkL2V",
      "ontid": "did:ont:AV9JgC4yGQn8u66XqcUbj7dP86dwLPaYpQ",
      "Intro": "Blockchain Global Limited is a multi-national enterprise founded in 2014, with offices in Melbourne, Perth, Lithuania, Kuala Lumpur, Shanghai, and Suzhou. The business focus is capital investment and incubation, blockchain technology development, media and communication. Blockchain Global Limited since its inception, through its multiple investment subsidiaries and funds, has invested blockchain projects of more than 50 companies and non-profit organizations, including but not limited to DigitalX, Huobi Australia, Blockchain Centre, Shape Capital, ACX.io, First Growth Fund Ltd, SaltLending, Powerledger, SingularityNet, and Penta.",
      "Website": "https://www.blockchainglobal.com/",
      "Vision": "In our vision, our core responsibility to the Ontology community will be to help govern and manage an Ontology seed node and build local Ontology community in Australia. We believe running and governing an Ontology seed node will be a co-operative effort, it’s important for the decentralized nature of the blockchain governance mechanisms that community member in the ecosystem will have equal benefits and achieve their personal development. We will make every effort to make this happen and we may need the cooperation and efforts contributed by Ontology community as well."
    },
    {
      "nodename": "HZF Web",
      "publickey": "0292c695bb734f849d17ee65977d048ff8a9efae8317ee40589a944eb2bbcd9998",
      "address": "AZzcK8m8ZiEUghfCJkmCSvi7cGam4YqXLk",
      "ontid": "did:ont:ARZv4ETT1LE8itaAATkjXfGQ5nskYuaCh2",
    },
    {
      "nodename": "Crypto World",
      "publickey": "0280d0bfde8e6b73e48c15ee062728f890e41f125339d5f36ed8a29430d41c6d57",
      "address": "AbewX26QiwLjQzT2naoPbtzqSCEVvsNkuv",
      "ontid": "did:ont:AWKLgApUpjVyPVRmyQFQNA2FAsYorCXsi7",
      "Intro": "The Crypto World Fund is an actively managed portfolio of crypto assets.",
      "Vision": "We can help the Ontology ecosystem with docking dApp applications as some of the team already have experience in landing and blockchain project development. Independent technology developers can help maintain the relevant communities and maintain the day-to-day management of the community and the routine maintenance of the project. We can also help Ontology develop Australia's local offline and online communities to expand the ONT ecosystem and its own community."
    },
    {
      "nodename": "Points Foundation",
      "publickey": "035960c0a6b07532e149fd8e92d19f4670732735df5c0a419a9d79c1a06df8fc23",
      "address": "AMJskicSD18QzVYcx5o4F6d67dbG4kKW7v",
      "ontid": "did:ont:ARU89iSYcLkiReH5b5KZ1z9zUcR34XmPwJ",
      "ip": "18.231.197.51",
    },


    {
      "nodename": "FreeS Crypto Fund",
      "publickey": "03569980d7fd460873767c0baac2d783f88617966ecb3dee1e7b7075b224463610",
      "address": "AQ3wagLbjh65Cf5RgG7MqjnUh2o7YLthYN",
      "ontid": "did:ont:AU28ptHRucTFN1qT55z8Pzpga43XmSP5Rg",
    },
    {
      "nodename": "Krypto Knight",
      "publickey": "03cb9417260995baf9781a58cb63db7e8bab2f8fdac193c27477e8e1b9eadecfae",
      "address": "AWGrHN1DUAo6Ao3yTHu4tUHZonPNAy9ZmU",
      "ontid": "did:ont:Af1DV2hEEDN2oo1UPQ3iCZFsnGaxksSwaL",
    },
    {
      "nodename": "Midwest Blockchain Club",
      "publickey": "03193e33a8373c480cb1b6dfec0d255364307dfc7072d6dd137041f5f240275a6a",
      "address": "ASPPqj8yCcCV2sWHQZwsYfWZS2FMfd3PF7",
      "ontid": "did:ont:ASPbda9sU8LrTgCVBRk2ECBFJL3Dzpo24z",
    },
    {
      "nodename": "SNZ Holding",
      "publickey": "036688d730248832b3cfc54a964201ff82dc212b73aa686629729ed3f11f26949e",
      "address": "AaxbVD8M3dV2fpxFbkP1yhgPB1J45tWpYa",
      "ontid": "did:ont:AWwiJHgUkUFyoiq2d13uvQ9ZMrJubZB8fs",
    },
    {
      "nodename": "Dragon",
      "publickey": "024477ed535fdf33e3f5251be5ddde8269bbc5bfa6eff253f3489ce9c1b72a27e2",
      "address": "Ac8P8376ozoQ5H2Srcm32n5yb8kLoixRaP",
      "ontid": "did:ont:AMmi2ANxyMK2jdd2Pd4dGWfSkrmXn56M5u",
    },

    {
      "nodename": "ZhenFund",
      "publickey": "02a529dc41498ecc989c43f03e6dc19e4798d0367fadec92cd49d4bec7e5cdee4c",
      "address": "AM5TmRiR32fZSYedcWHxEBScLBPZ67hdER",
      "ontid": "did:ont:ARagutv7Sw8MPdGTjSRsT3UMP4LjPUqjdz",
    },
    {
      "nodename": "Fourier",
      "publickey": "029d393356cd12e7e74b079234e8d014d878f961968646f6121b597dcaec43a757",
      "address": "AP3ub6e7xeuw3Lr3SgUSTxYxxxNjTt3kXx",
      "ontid": "did:ont:ANSZiNXysbqE78DPxJcEkGahrDz299a2D6",
    },
    {
      "nodename": "Cobo",
      "publickey": "02b325b47c80fa21967577cd748ffb6ed41e4dcb5d8b2d7e400d39d4b434827562",
      "address": "AcdH5iCT5DSxUio29YMykT8eakgbjYeWBW",
      "ontid": "did:ont:AV2Geuk7WJ15cUmMW5HxJReF2gEFVRLyTu",
    },
    {
      "nodename": "Timestamp Capital",
      "publickey": "0385bed5ddf7c45bfa586654c443af899377943343866e4da10705166d9dc2a124",
      "address": "AbGDhXXyjHLBc53BDR8jrRZLAL1BteL7VA",
      "ontid": "did:ont:ASssrQfeTpqUEMhEU7UYaj7g3TgDj1NrkT",
    },
    {
      "nodename": "Marvelous Peach Capital",
      "publickey": "03e7bedc0845ced9f9c34131e8582abe60db164152d5e265054e87ecffd44e8aaf",
      "address": "AFsfeivZ1iTbL1sqY8UkTZ8kqygwGerDNj",
      "ontid": "did:ont:AUDnDjyiWs3HW551S4ZAcnT1VGUEA1jxtW",
    },
    {
      "nodename": "BlockBoost",
      "publickey": "02f15de2033784e595fcb27e4b111b1dbebe55d1cd1f5d9f46792fe193851f3433",
      "address": "AKshHCFGWHMftXELBmogxrjrDMW61xgph9",
      "ontid": "did:ont:AeY65vMDgKotVogNAJzUs2fTbwgA1X9cUt",
    },

    {
      "nodename": "PreAngel",
      "publickey": "032f6464df7c42b5a80953680165a23cb98453a1fcb5770f233664909847faf36f",
      "address": "ALaDrS5ZwMKZgTS3a8okgDDz84k3ttfP4x",
      "ontid": "did:ont:AKLCERJ5KikFG4EVGDxK48uahdouAWqk6A",
    },
    {
      "nodename": "Longlink Fund",
      "publickey": "03199dcc2f89a54f171292e3e6c87472f742edefb7d4fe8685e24486262ca01f15",
      "address": "Aa1MF3pTq4CaE3HK4umgZL3WxLh3A1CiBH",
      "ontid": "did:ont:AV34rhrAZaPL7cmcyDsaLn5SoKNrf5ch29",
    },
    {
      "nodename": "Nebuchadnezzar Limited",
      "publickey": "033fb4fa791dc229b83c5e88a1e0c861f429b10a4551e02d33bab19a08640e4727",
      "address": "AModfYVLuvvaacsexSBAvegnykog5yH2Ji",
      "ontid": "did:ont:AFvBtfFHd5s7aCT3qNfxtAVUhmeS48WhDM",
    },
    {
      "nodename": "Accomplice Blockchain Two - International",
      "publickey": "03d0492bed2f45c78a88fb6f5e1ab2192bb3de43f72ba81774c32d6499e99d7ad9",
      "address": "ANumnYcRtbT1XxCw1hs9WGjJaDURMxiuQ9",
      "ontid": "did:ont:ARdphNxfxzkXmzCcnmaqHUzKa6MEXrsQbL",
    },
    {
      "nodename": "Abine",
      "publickey": "02f0e43592212629ff5f482a0011eba5c5be059cd40eb1bdd2c956c1139967bb35",
      "address": "AV59sm9kRGB4EYRKCMYXXsiCPKzbAFMcpA",
      "ontid": "did:ont:AdiYHQJkfGC1PjMP9KePMAcoPJrVpBW6RB",
    },
    {
      "nodename": "Huobi Wallet",
      "publickey": "02fcf82dbd1952fa073d47c831d35fdb3bc264edc355962e3d9332060a61c962cd",
      "address": "ATTzSUQm5MgXQCLfrbWBv9hSBLcZX75giR",
      "ontid": "did:ont:AanoxBZqh2fzdfWuKckQXHcjeMRRupdpW7",
    }
  ]
}

/**
 * 根据公钥获取ont id
 *
 * @param $pk
 * @return {string}
 */
function getItemData($pk) {
  let allData = nodeDetailData();

  for (let i in allData) {
    if (allData[i].publickey === $pk)
      return allData[i]
  }
}

export default {
  state: {
    AuthorizationList: {},
    NodeInfo: {},
    Countdown: 0,
    fetchProcess: 0
  },
  mutations: {
    [types.UPDATE_NODE_LIST](state, payload) {
      state.AuthorizationList = payload.info
    },
    [types.UPDATE_COUNTDOWN_BLOCK](state, payload) {
      state.Countdown = payload.info;
    },
    [types.UPDATE_NODE_INFO](state, payload) {
      state.NodeInfo = payload.info;
    },
    [types.UPDATE_FETCH_PROCESS](state, payload) {
      state.fetchProcess = payload.info;
    }
  },
  actions: {
    async fetchNodeList({commit}, params) {
      let url = (params.net === 'testnet') ? process.env.TEST_DAPP_NODE_URL : process.env.DAPP_NODE_URL;

      try {
        const peerMap = await Ont.GovernanceTxBuilder.getPeerPoolMap(url);
        const list = [];

        let processNum = 0;
        let processLength = Object.keys(peerMap);
        processLength = processLength.length;

        for (let k in peerMap) {
          let item = peerMap[k];
          const attr = await Ont.GovernanceTxBuilder.getAttributes(item.peerPubkey, url);
          item.maxAuthorize = attr.maxAuthorize;
          item.maxAuthorizeStr = numeral(item.maxAuthorize).format('0,0');
          item.totalPosStr = numeral(item.totalPos).format('0,0');
          // const nodeProportion = attr.newPeerCost + '%';
          // const userProportion = (100 - attr.newPeerCost) + '%';
          // item.nodeProportion = nodeProportion + ' / ' + userProportion;
          item.nodeProportion = (100 - attr.t1PeerCost) + '%';

          // 只有1和2显示
          if (item.status === 1 || item.status === 2) {
            list.push(item)
          }

          commit({
            type: types.UPDATE_FETCH_PROCESS,
            info: ((processNum + 1) / processLength) * 100
          });
          processNum = processNum + 1;
        }

        list.sort((v1, v2) => {
          if ((v2.initPos + v2.totalPos) > (v1.initPos + v1.totalPos)) {
            return 1;
          } else if ((v2.initPos + v2.totalPos) < (v1.initPos + v1.totalPos)) {
            return -1;
          } else {
            return 0;
          }
        });

        list.forEach((item, index) => {
          item.rank = index + 1;
          item.address = item.address.toBase58();
          item.currentStake = item.initPos + item.totalPos;
          item.process = Number((item.totalPos + item.initPos) * 100 / (item.initPos + item.maxAuthorize)).toFixed(2) + '%';
          item.pk = item.peerPubkey;

          let nodeData = getItemData(item.pk);

          item.ontId = nodeData.ontid
          item.name = nodeData.nodename
          if (item.peerPubkey === '02f4c0a18ae38a65b070820e3e51583fd3aea06fee2dc4c03328e4b4115c622567') {//for test
            item.name = 'Node1 To Authorize'
          }
          if (item.pk === '03f6149b3a982c046912731d6374305e2bc8e278fa90892f6f20a8ee85c1d5443f') {//for test
            item.name = 'Node2 To Authorize'
          }
        });

        commit({
          type: types.UPDATE_NODE_LIST,
          info: list
        })
      } catch (err) {
        console.log(err);
      }
    },
    async fetchBlockCountdown({commit}, params) {
      let url = (params.net === 'testnet') ? process.env.TEST_DAPP_NODE_URL : process.env.DAPP_NODE_URL;

      const rest = new Ont.RestClient(url);
      try {
        const view = await Ont.GovernanceTxBuilder.getGovernanceView(url);
        const blockRes = await rest.getBlockHeight();
        const blockHeight = blockRes.Result;
        const countdown = 120000 - (blockHeight - view.height);

        commit({
          type: types.UPDATE_COUNTDOWN_BLOCK,
          info: countdown,
        })
      } catch (err) {
        console.log(err)
      }
    },
    getNodeInfo({commit}, params) {
      let info = getItemData(params);

      commit({
        type: types.UPDATE_NODE_INFO,
        info: info,
      })
    }
  }
}
