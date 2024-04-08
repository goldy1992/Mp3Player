import type { GatsbyConfig } from "gatsby";


const config: GatsbyConfig = {
  siteMetadata: {
    title: `MP3 Player`,
    siteUrl: `https://goldy1992.github.io/Mp3Player/`
  },
  // More easily incorporate content into your pages through automatic TypeScript type generation and better GraphQL IntelliSense.
  // If you use VSCode you can also use the GraphQL plugin
  // Learn more at: https://gatsby.dev/graphql-typegen
  graphqlTypegen: true,
  plugins: [
    "gatsby-plugin-root-import", 
    "gatsby-plugin-sharp",
    "gatsby-transformer-sharp",
    "gatsby-plugin-postcss",
    {
          resolve: 'gatsby-source-filesystem',
          options: {
            "name": "images",
            "path": "./src/images/"
          },
          __key: "images"
        }, {
          resolve: 'gatsby-source-filesystem',
          options: {
            "name": "pages",
            "path": "./src/pages/"
          },
          __key: "pages"
        },
    "gatsby-plugin-sitemap", 
    {
      resolve: 'gatsby-plugin-manifest',
      options: {
        name: `MP3 Player Webpage`,
        short_name: `MP3 Player`,
        start_url: `/Mp3Player`,
        icon: `src/assets/logo.svg`,
      },
    }, 
    "gatsby-plugin-tsconfig-paths",
    {
      resolve: "gatsby-plugin-react-svg",
      options: {
        rule: {
          include: /assets/ // See below to configure properly
        }
      }
    },

],

};

// module.exports = {
//   pathPrefix: "/mp3-player",
// }

export default config;
