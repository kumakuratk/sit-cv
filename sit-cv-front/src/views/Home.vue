<template>
  <dev>
    <table>
      <thead>
        <tr><th>EntryPoint</th></tr>
      </thead>
      <tbody>
        <tr v-for="entryPoint in entryPoints">
          <td>
            <router-link :to="entryPoint.url">{{ entryPoint.id }}</router-link>
          </td>
      </tbody>
    </table>
  </dev>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import DesignDocService from '@/domains/designdoc/DesignDocService';
import DesignDocServiceFactory from '@/domains/designdoc/DesignDocServiceFactory';
import EntryPoint from '@/domains/designdoc/EntryPoint';

@Component
export default class Home extends Vue {
  designDocService: DesignDocService = DesignDocServiceFactory.getService();

  entryPoints: EntryPoint[] = [];
  created() {
    this.designDocService.fetchEntryPoint((entryPoints) => (this.entryPoints = entryPoints));
  }
}
</script>
