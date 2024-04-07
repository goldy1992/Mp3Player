// Allows the import of SVG files and allows VS Code t o recognise them
declare module "*.svg" {
    const content: any;
    export default content;
  }